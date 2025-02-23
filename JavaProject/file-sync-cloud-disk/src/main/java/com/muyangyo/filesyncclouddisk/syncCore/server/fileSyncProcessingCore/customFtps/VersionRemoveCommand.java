package com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.customFtps;

import com.muyangyo.filesyncclouddisk.common.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.command.AbstractCommand;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.impl.FtpIoSession;
import org.apache.ftpserver.impl.FtpServerContext;
import org.apache.ftpserver.impl.LocalizedFtpReply;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;


@Slf4j
public class VersionRemoveCommand extends AbstractCommand {
    public static final String COMMAND_NAME = "VER_RM"; // 版本保留性删除

    private static final int SUCCESS = 200; // 操作成功
    private static final int FILE_NOT_FOUND = 550;// 文件不存在
    private static final int SYNTAX_ERROR = 501;// 语法错误


    private final boolean ENABLE_VERSION_DELETE;
    private final int MAX_VERSIONS;
    private final String VERSION_DEL_DIR;

    public VersionRemoveCommand(boolean enableVersionDelete, int maxVersions, String versionDelDir) {
        this.ENABLE_VERSION_DELETE = enableVersionDelete;
        this.MAX_VERSIONS = maxVersions;
        this.VERSION_DEL_DIR = versionDelDir;
    }


    private static LocalizedFtpReply getResponse(int code, String message) {
        return new LocalizedFtpReply(code, message != null ? message : "");
    }

    @Override
    public void execute(FtpIoSession session, FtpServerContext context, FtpRequest request)
            throws IOException {
        session.resetState();

        String remotePath = request.getArgument();
        log.info("执行{}命令,参数 [{}]", COMMAND_NAME, remotePath);

        // 参数校验
        if (!StringUtils.hasLength(remotePath)) {
            session.write(getResponse(SYNTAX_ERROR, "路径参数为空"));
            return;
        }

        // 获取用户主目录
        String homeDir = session.getUser().getHomeDirectory();
        Path fullPath = Paths.get(homeDir, remotePath).normalize();

        // 检查文件是否存在
        if (!Files.exists(fullPath)) {
            session.write(getResponse(FILE_NOT_FOUND, "文件不存在"));
            return;
        }

        try {
            if (ENABLE_VERSION_DELETE) {
                // 创建版本目录
                if (!FileUtils.exists(VERSION_DEL_DIR)) {
                    FileUtils.createHiddenDirectory(VERSION_DEL_DIR);
                }

                // 获取文件的基本信息
                Path path = Paths.get(remotePath);
                String fileParent = path.getParent() == null ? "" : path.getParent().toString();
                String fileNameWithoutExtension = FileUtils.getFileNameWithoutExtension(remotePath);
                String fileExtension = FileUtils.getFileExtension(remotePath);


                // 创建对应路径的文件夹
                String realParentPath = Paths.get(VERSION_DEL_DIR, session.getUser().getName(), fileParent).toString();
                if (!FileUtils.exists(realParentPath)) {
                    FileUtils.createDirectory(realParentPath);
                }

                // 获取当前版本文件列表
                File backupParentFile = new File(realParentPath);
                File[] backupFiles = backupParentFile.listFiles((dir, name) ->
                        name.startsWith(fileNameWithoutExtension + " - ") && name.endsWith(fileExtension));

                // 按版本号排序
                if (backupFiles != null && backupFiles.length > 0) {
                    Arrays.sort(backupFiles, Comparator.comparing(File::getName));
                }

                // 如果版本数量超过最大值，删除最旧的版本
                if (backupFiles != null && backupFiles.length >= MAX_VERSIONS) {
                    File oldestFile = backupFiles[0];
                    if (oldestFile.delete()) {
                        log.info("删除最旧版本文件 [{}]", oldestFile.getAbsolutePath());
                    } else {
                        log.error("删除最旧版本文件失败 [{}]", oldestFile.getAbsolutePath());
                    }
                }

                // 生成新版本号
                int newVersion = 1;
                if (backupFiles != null && backupFiles.length > 0) {
                    String lastFileName = backupFiles[backupFiles.length - 1].getName();
                    String lastVersionStr = lastFileName.substring(
                            lastFileName.lastIndexOf(" - ") + 3,
                            lastFileName.lastIndexOf(".")
                    );
                    newVersion = Integer.parseInt(lastVersionStr) + 1;
                }

                // 生成新版本文件名
                String newBackupFileName = fileNameWithoutExtension + " - " + newVersion + "." + fileExtension;
                Path newBackupPath = Paths.get(realParentPath, newBackupFileName);

                // 移动文件到版本目录
                Files.move(fullPath, newBackupPath, StandardCopyOption.REPLACE_EXISTING);
                log.info("文件版本保留成功 [{}] -> [{}]", fullPath, newBackupPath);

                session.write(getResponse(SUCCESS, COMMAND_NAME + " OK VERSION DELETE"));
            } else {
                // 直接删除文件
                Files.delete(fullPath);
                session.write(getResponse(SUCCESS, COMMAND_NAME + " OK DELETE"));
            }
        } catch (IOException e) {
            log.error("版本保留失败", e);
            session.write(getResponse(FILE_NOT_FOUND, "操作失败"));
        }
    }
}