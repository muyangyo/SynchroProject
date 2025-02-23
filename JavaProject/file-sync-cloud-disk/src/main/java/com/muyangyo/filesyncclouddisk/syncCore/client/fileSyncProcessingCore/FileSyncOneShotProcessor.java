package com.muyangyo.filesyncclouddisk.syncCore.client.fileSyncProcessingCore;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.utils.CRC32Util;
import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import com.muyangyo.filesyncclouddisk.common.utils.FtpsClientBuilder;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncInfoMapper;
import com.muyangyo.filesyncclouddisk.manager.service.SyncLogService;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.FileMetadata;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncDiff;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import com.muyangyo.filesyncclouddisk.syncCore.common.utils.EasyFTP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件同步管理类（实现rsync差异比较逻辑和执行同步） <br>
 * 这个一次只管一个文件夹的同步，如果需要多文件夹同步，可以创建多个FileSyncManager实例
 */
@Slf4j
public class FileSyncOneShotProcessor {

    private volatile SyncStatus SYNC_STATUS = SyncStatus.WAIT_CONNECT; // 同步状态(默认等待连接)

    /**
     * 设置同步状态
     *
     * @param status 状态枚举
     * @return true
     */
    public synchronized boolean setSyncStatus(SyncStatus status) {
        SYNC_STATUS = status;
        return true;
    }

    /**
     * 获取同步状态
     *
     * @return 同步状态枚举
     */
    public synchronized SyncStatus getSyncStatus() {
        return SYNC_STATUS;
    }

    private boolean isFirst; // 是否第一次启动（用于第一次全量同步)
    private final String localBasePath; // 需要监控的本地同步路径

    private final String serverIp; // FTPS服务端IP
    private final int ftpPort; // FTPS端口
    private final String ftpUsername; // FTPS用户名
    private final String ftpPassword; // FTPS密码
    //外部变量
    private final SyncInfoMapper syncInfoMapper; // 操作 配置 数据库的
    private final SyncLogService syncLogService; // 操作 日志 数据库的


    FileSyncOneShotProcessor(String localBasePath, boolean isFirst, String ftpServerIp, int ftpPort, String ftpUsername, String ftpPassword,
                             SyncInfoMapper syncInfoMapper, SyncLogService syncLogService) {
        this.localBasePath = localBasePath;
        this.isFirst = isFirst;
        this.serverIp = ftpServerIp;
        this.ftpPort = ftpPort;
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
        this.syncInfoMapper = syncInfoMapper;
        this.syncLogService = syncLogService;
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(ftpServerIp, ftpPort, ftpUsername, ftpPassword))) {
            SYNC_STATUS = SyncStatus.CONNECTING;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 执行全量/增量同步
     */
    public void syncFiles() throws IOException {
        SYNC_STATUS = SyncStatus.SYNCING;
        if (isFirst) {
            // 第一次启动，全量同步,下载整个目录
            downloadAllFiles();
            log.info("第一次启动，基于 [{}] 全量同步完成", localBasePath);
            syncLogService.addLog(SyncLogService.DOWNLOAD, "全量同步", ftpUsername, serverIp, OperationLevel.INFO);
            syncInfoMapper.updateLastSyncTimeByName(ftpUsername, new Date());
        } else {
            // 增量同步
            // 1. 获取本地当前文件状态
            Map<String, FileMetadata> currentState = scanLocalFiles();

            // 2. 获取服务端文件状态（通过FTPS）
            Map<String, FileMetadata> serverState = scanRemoteFiles();

            // 3. 生成差异报告
            SyncDiff diff = generateSyncDiff(currentState, serverState);
            log.info("差异报告: 本次基于 [{}] 的同步，需要上传的有 {} 个, 需要下载的有 {} 个, 需要删除的有 {} 个", localBasePath,
                    diff.getNeedUploadFiles().size(), diff.getNeedDownloadFiles().size(), diff.getNeedDeletedFiles().size());

            // 4. 执行同步操作
            processSync(diff);

            syncInfoMapper.updateLastSyncTimeByName(ftpUsername, new Date());
        }
        isFirst = false;
        SYNC_STATUS = SyncStatus.SYNC_COMPLETE;
    }

    private void downloadAllFiles() throws IOException {
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(serverIp, ftpPort, ftpUsername, ftpPassword))) {
            downloadFileRecursive(ftp, "/", localBasePath);
        }
    }

    private void downloadFileRecursive(EasyFTP ftp, String remotePath, String localPath) throws IOException {
        FileUtils.createDirectory(Paths.get(localPath).normalize().toString());

        // 列出远程目录下的所有文件和文件夹
        List<String> files = ftp.ls(remotePath);

        for (String file : files) {
            String remoteFilePath = (remotePath.equals("/") ? "" : remotePath) + "/" + file;
            String localFilePath = localPath + "/" + file;


            if (ftp.isDir(remoteFilePath)) {
                // 如果是目录，递归下载
                downloadFileRecursive(ftp, remoteFilePath, localFilePath);
            } else {
                // 如果是文件，直接下载
                ftp.download(remoteFilePath, new File(localFilePath));
            }
        }
    }

    /**
     * 扫描本地文件生成元数据
     */
    private Map<String, FileMetadata> scanLocalFiles() throws IOException {
        Map<String, FileMetadata> metadataMap = new HashMap<>();
        Files.walkFileTree(Paths.get(localBasePath), new SimpleFileVisitor<Path>() {
            @Override
            // 访问文件获取元数据
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    FileMetadata meta = new FileMetadata(
                            FileUtils.getAbsolutePath(file.toString()),
                            attrs.size(),
                            attrs.lastModifiedTime().toMillis(),
                            CRC32Util.calculateCRC32(file.toFile())
                    );
                    metadataMap.put(FileUtils.getAbsolutePath(file.toString()), meta);
                } catch (IOException e) {
                    log.warn("无法读取文件元数据: {}", file, e);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return metadataMap;
    }

    /**
     * 获取远程文件状态（通过FTPS）
     *
     * @return 远程文件状态
     */
    private Map<String, FileMetadata> scanRemoteFiles() throws IOException {
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(serverIp, ftpPort, ftpUsername, ftpPassword))) {
            Map<String, FileMetadata> serverState = new HashMap<>();
            scanRemoteFilesRecursive(ftp, "/", serverState);
            return serverState;
        }
    }

    private void scanRemoteFilesRecursive(EasyFTP ftp, String remoteDir, Map<String, FileMetadata> serverState) {
        try {
            // 检查远程目录是否存在
            if (!ftp.exist(remoteDir)) {
                log.warn("远程目录不存在: {}", remoteDir);
                return;
            }

            // 获取目录下的文件列表（包含详细信息）
            List<FTPFile> ftpFiles = ftp.lsFiles(remoteDir, null);
            for (FTPFile ftpFile : ftpFiles) {
                String fileName = ftpFile.getName();
                // 处理路径拼接
                String remotePath = Paths.get((remoteDir.equals("/") ? "" : remoteDir), fileName).toString().replace("\\", "/");

                if (ftpFile.isDirectory()) {
                    // 递归遍历子目录
                    scanRemoteFilesRecursive(ftp, remotePath, serverState);
                } else {
                    // 生成文件元数据
                    long size = ftpFile.getSize();
                    long lastModified = ftpFile.getTimestamp().getTimeInMillis();
                    long crc32 = ftp.getFileCRC32(remotePath);
                    serverState.put(remotePath, new FileMetadata(remotePath, size, lastModified, crc32));
                }
            }
        } catch (IOException e) {
            log.error("扫描远程目录失败: {}", remoteDir, e);
        }
    }

    /**
     * 生成同步差异报告
     */
    private SyncDiff generateSyncDiff(Map<String, FileMetadata> local,
                                      Map<String, FileMetadata> remote) {
        SyncDiff diff = new SyncDiff();

        // 检测新增/修改文件
        for (Map.Entry<String, FileMetadata> entry : local.entrySet()) {
            String path = entry.getKey();// 文件路径
            FileMetadata localMeta = entry.getValue(); // 本地文件元数据
            FileMetadata remoteMeta = remote.get(getRemotePath(path)); // 远程文件元数据,记得转化路径

            if (remoteMeta == null) {
                // 1.如果远程不存在,本地存在，则新增
                diff.addUploadFile(localMeta.getPath());
            } else if (!localMeta.equals(remoteMeta)) {
                // 如果远程存在但内容不同，根据新盖旧 原则
                if (remoteMeta.getLastModified() < localMeta.getLastModified()) {
                    // 2.本地文件更新，上传
                    diff.addUploadFile(localMeta.getPath());
                } else {
                    // 3.远程文件更新，下载
                    diff.addDownloadFile(remoteMeta.getPath());
                }
            }
        }

        // 4.如果远程存在.本地不存在，删除远程文件
        for (String remotePath : remote.keySet()) {
            if (!local.containsKey(getLocalPath(remotePath))) {
                diff.addDeletedFile(remotePath);
            }
        }
        // 都不存在不管

        return diff;
    }

    /**
     * 处理同步操作
     */
    private void processSync(SyncDiff diff) throws IOException {
        for (String path : diff.getNeedUploadFiles()) {
            uploadFile(path, serverIp, ftpPort, ftpUsername, ftpPassword);
        }
        for (String path : diff.getNeedDownloadFiles()) {
            downloadFile(path, serverIp, ftpPort, ftpUsername, ftpPassword);
        }
        for (String path : diff.getNeedDeletedFiles()) {
            deleteRemoteFile(path, serverIp, ftpPort, ftpUsername, ftpPassword);
        }
    }

    private void deleteRemoteFile(String remotePath, String serverIp, int ftpPort, String ftpUsername, String ftpPassword) throws IOException {
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(serverIp, ftpPort, ftpUsername, ftpPassword))) {
            boolean b = ftp.versionRemoveFile(remotePath);
            if (b) {
                log.info("文件删除成功：远程 [/{}]", remotePath);
                syncLogService.addLog(SyncLogService.DELETE, "远程文件: " + remotePath, ftpUsername, serverIp, OperationLevel.IMPORTANT);
            }
        }
    }

    private void downloadFile(String remotePath, String serverIp, int ftpPort, String ftpUsername, String ftpPassword) throws IOException {
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(serverIp, ftpPort, ftpUsername, ftpPassword))) {
            Path tmpPath = Paths.get(remotePath); // 临时文件路径
            String remoteDir = tmpPath.getParent() == null ? "/" : tmpPath.getParent().toString(); // 远程目录
            String fileName = tmpPath.getFileName().toString(); // 文件名

            String localPath = getLocalPath(remotePath);// 本地路径
            ftp.download(remoteDir, fileName, new File(localPath));
            syncLogService.addLog(SyncLogService.DOWNLOAD, remotePath, ftpUsername, serverIp, OperationLevel.INFO);
            log.info("文件下载成功：远程 [{}] -> [{}]", remoteDir + remotePath, localPath);
        }
    }

    /**
     * 实现文件上传
     */
    private void uploadFile(String localPath, String serverIp, int ftpPort, String ftpUsername, String ftpPassword) throws IOException {
        try (EasyFTP ftp = new EasyFTP(FtpsClientBuilder.build(serverIp, ftpPort, ftpUsername, ftpPassword))) {
            String remotePath = getRemotePath(localPath);// 远程路径
            Path tmpPath = Paths.get(remotePath);// 临时文件路径
            String remoteDir = tmpPath.getParent() == null ? "/" : tmpPath.getParent().toString(); // 远程目录
            String fileName = tmpPath.getFileName().toString(); // 文件名
            ftp.upload(remoteDir, fileName, new File(localPath));
            syncLogService.addLog(SyncLogService.ADD, localPath, ftpUsername, serverIp, OperationLevel.WARNING);
            log.info("文件上传成功：[{}] -> 远程 [{}]", localPath, remoteDir + remotePath);
        }
    }


    /**
     * 通过本地路径获取远程路径
     *
     * @param localPath 本地路径
     * @return 远程路径
     */
    private String getRemotePath(String localPath) {
        try {
            Path localBase = Paths.get(localBasePath).toAbsolutePath().normalize();
            Path filePath = Paths.get(localPath).toAbsolutePath().normalize();

            if (filePath.startsWith(localBase)) {
                // 转换为相对路径并统一为 Unix 风格
                return localBase.relativize(filePath).toString().replace(File.separator, "/");
            } else {
                throw new IllegalArgumentException("文件路径不在监控范围内: " + localPath);
            }
        } catch (InvalidPathException e) {
            throw new IllegalArgumentException("无效的路径格式: " + localPath, e);
        }
    }

    /**
     * 通过远程路径获取本地路径
     *
     * @param remotePath 远程路径
     * @return 本地路径
     */
    private String getLocalPath(String remotePath) {
        if (remotePath.startsWith("/")) {
            remotePath = remotePath.substring(1);
        }
        return localBasePath + "/" + remotePath.replace(File.separator, "/");
    }
}

