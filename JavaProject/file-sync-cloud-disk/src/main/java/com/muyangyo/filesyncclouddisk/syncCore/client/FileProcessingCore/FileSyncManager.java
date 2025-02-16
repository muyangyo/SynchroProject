package com.muyangyo.filesyncclouddisk.syncCore.client.FileProcessingCore;

import com.muyangyo.filesyncclouddisk.common.utils.ChecksumUtil;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.FileMetadata;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncDiff;
import com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 文件同步管理类（实现rsync差异比较逻辑）
 */
@Slf4j
public class FileSyncManager {
    private final Map<String, FileMetadata> lastSyncState = new HashMap<>(); // 上一次同步状态
    private final Set<String> monitoredPaths = new HashSet<>(); // 需要监控的同步路径

    /**
     * 添加需要监控的同步路径
     *
     * @param path 需要同步的目录路径
     */
    public void addSyncPath(String path) {
        monitoredPaths.add(path);
        log.info("添加同步路径: {}", path);
    }

    /**
     * 执行全量/增量同步
     *
     * @param serverIp 服务端IP
     * @param ftpPort  FTPS端口
     */
/*    public void syncFiles(String serverIp, int ftpPort) {
        try {
            // 1. 获取本地当前文件状态
            Map<String, FileMetadata> currentState = scanLocalFiles();

            // 2. 获取服务端文件状态（通过FTPS）
            Map<String, FileMetadata> serverState = fetchServerState(serverIp, ftpPort);

            // 3. 生成差异报告
            SyncDiff diff = generateSyncDiff(currentState, serverState);

            // 4. 执行同步操作
            processSync(diff, serverIp, ftpPort);

            // 5. 更新同步状态
            lastSyncState.clear();
            lastSyncState.putAll(currentState);

        } catch (Exception e) {
            log.error("文件同步失败", e);
        }
    }*/

    /**
     * 扫描本地文件生成元数据
     */
    @SneakyThrows
    private Map<String, FileMetadata> scanLocalFiles() {
        Map<String, FileMetadata> metadataMap = new HashMap<>();

        for (String path : monitoredPaths) {
            Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
                @SneakyThrows
                @Override
                // 访问文件获取元数据
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        FileMetadata meta = new FileMetadata(
                                file.toString(),
                                attrs.size(),
                                attrs.lastModifiedTime().toMillis(),
                                ChecksumUtil.calculateCRC32(file.toFile())
                        );
                        metadataMap.put(file.toString(), meta);
                    } catch (IOException e) {
                        log.warn("无法读取文件元数据: {}", file, e);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
        return metadataMap;
    }

    /**
     * 从服务端获取文件状态（通过FTPS协议）
     */
/*    private Map<String, FileMetadata> fetchServerState(String serverIp, int ftpPort) {
        Map<String, FileMetadata> serverState = new HashMap<>();
        try (FTPSClientWrapper ftpWrapper = new FTPSClientWrapper(serverIp, ftpPort)) {
            FTPSClient ftp = ftpWrapper.getClient();
            // 获取所有监控路径对应的远程目录
            for (String localPath : monitoredPaths) {
                String remoteDir = getRemoteDir(localPath); // 路径映射关系 todo: 路径映射规则
                ftp.changeWorkingDirectory(remoteDir);

                // 使用MLSD获取详细文件列表
                for (FTPFile ftpFile : ftp.mlistDir(remoteDir)) {
                    String remotePath = remoteDir + "/" + ftpFile.getName();
                    serverState.put(remotePath, new FileMetadata(
                            remotePath,
                            ftpFile.getSize(),
                            ftpFile.getTimestamp().getTimeInMillis(),
                            fetchRemoteChecksum(ftp, remotePath) // 需要服务端支持校验和命令 todo: 校验和算法
                    ));
                }
            }
        } catch (Exception e) {
            log.error("获取服务端状态失败", e);
        }
        return serverState;
    }*/

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
            FileMetadata remoteMeta = remote.get(path); // 远程文件元数据

            if (remoteMeta == null) {
                diff.addNewFile(path);
            } else if (!localMeta.equals(remoteMeta)) {
                diff.addModifiedFile(path);
            }
        }

        // 检测删除文件
        for (String remotePath : remote.keySet()) {
            if (!local.containsKey(remotePath)) {
                diff.addDeletedFile(remotePath);
            }
        }

        return diff;
    }

    /**
     * 处理同步操作
     */
    private void processSync(SyncDiff diff, String serverIp, int ftpPort) {
        // 上传新增/修改文件
        for (String path : diff.getNewFiles()) {
            uploadFile(path, serverIp, ftpPort);
        }
        for (String path : diff.getModifiedFiles()) {
            uploadFile(path, serverIp, ftpPort);
        }

        // 删除多余文件
        for (String path : diff.getDeletedFiles()) {
            deleteRemoteFile(path, serverIp, ftpPort);
        }
    }

    /**
     * 实现文件上传
     */
    private void uploadFile(String localPath, String serverIp, int ftpPort) {
        try (FTPSClientWrapper ftpWrapper = new FTPSClientWrapper(serverIp, ftpPort)) {
            FTPSClient ftp = ftpWrapper.getClient();
            File localFile = new File(localPath);
            try (InputStream is = Files.newInputStream(localFile.toPath())) {
                // 获取相对路径（根据监控路径转换）
                String remotePath = getRemotePath(localFile.getAbsolutePath());
                ftp.storeFile(remotePath, is);
                log.info("文件上传成功：{} -> {}", localPath, remotePath);
            }
        } catch (Exception e) {
            log.error("文件上传失败：{}", localPath, e);
        }
    }


    /**
     * 删除远程文件
     */
    private void deleteRemoteFile(String remotePath, String serverIp, int ftpPort) {
        try (FTPSClientWrapper ftpWrapper = new FTPSClientWrapper(serverIp, ftpPort)) {
            FTPSClient ftp = ftpWrapper.getClient();
            if (ftp.deleteFile(remotePath)) {
                log.info("远程文件删除成功：{}", remotePath);
            }
        } catch (Exception e) {
            log.error("远程文件删除失败：{}", remotePath, e);
        }
    }


    // 路径映射方法（示例）
    private String getRemotePath(String absoluteLocalPath) {
        for (String basePath : monitoredPaths) {
            if (absoluteLocalPath.startsWith(basePath)) {
                return absoluteLocalPath.substring(basePath.length());
            }
        }
        return absoluteLocalPath;
    }

    /**
     * FTPSClient 包装类，用于管理 FTPSClient 的生命周期
     */
    private static class FTPSClientWrapper implements AutoCloseable {
        private final FTPSClient ftp;

        public FTPSClientWrapper(String serverIp, int ftpPort) throws Exception {
            this.ftp = createFTPSClient(serverIp, ftpPort);
        }

        public FTPSClient getClient() {
            return ftp;
        }

        @Override
        public void close() throws Exception {
            if (ftp != null && ftp.isConnected()) {
                ftp.logout();
                ftp.disconnect();
            }
        }

        private FTPSClient createFTPSClient(String serverIp, int ftpPort) throws Exception {
            FTPSClient ftp = new FTPSClient(true); // 隐式FTPS
            ftp.connect(serverIp, ftpPort); // 连接FTPS服务器
            ftp.login(ServerMain.FTPS_USER_NAME, ServerMain.FTPS_SERVER_OA_PASSWORD); // 登录FTPS服务器
            ftp.execPBSZ(0); // 设置保护缓冲区大小
            ftp.execPROT("P"); // 启用数据通道加密
            ftp.enterLocalPassiveMode(); // 进入被动模式(21端口是指令通道,但是这里是通知服务器开启数据通道的端口)
            return ftp; // 返回FTPS客户端
        }
    }

}