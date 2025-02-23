package com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.customFtps;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.manager.service.SyncLogService;
import org.apache.ftpserver.ftplet.*;

import java.io.IOException;

public class OperationLoggingFtplet extends DefaultFtplet {
    private final SyncLogService syncLogService;

    public OperationLoggingFtplet(SyncLogService syncLogService) {
        this.syncLogService = syncLogService;
    }

    @Override
    public FtpletResult onUploadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String operator = session.getUser().getName(); // 操作者（FTP 用户名）
        String ip = session.getClientAddress().getAddress().getHostAddress(); // 操作 IP
        syncLogService.addLog(SyncLogService.ADD, request.getArgument(), operator, ip, OperationLevel.WARNING);
        return super.onUploadStart(session, request);
    }

    @Override
    public FtpletResult onDownloadStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String operator = session.getUser().getName(); // 操作者（FTP 用户名）
        String ip = session.getClientAddress().getAddress().getHostAddress(); // 操作 IP
        syncLogService.addLog(SyncLogService.DOWNLOAD, request.getArgument(), operator, ip, OperationLevel.INFO);
        return super.onDownloadStart(session, request);
    }

    @Override
    public FtpletResult onDeleteStart(FtpSession session, FtpRequest request) throws FtpException, IOException {
        String operator = session.getUser().getName(); // 操作者（FTP 用户名）
        String ip = session.getClientAddress().getAddress().getHostAddress(); // 操作 IP
        syncLogService.addLog(SyncLogService.DELETE, request.getArgument(), operator, ip, OperationLevel.IMPORTANT);
        return super.onDeleteStart(session, request);
    }
    @Override
    public FtpletResult afterCommand(FtpSession session, FtpRequest request, FtpReply reply) throws FtpException, IOException {
        String command = request.getCommand().toUpperCase();
        String argument = request.getArgument();

        // 捕获 VER_RM 命令
        if (VersionRemoveCommand.COMMAND_NAME.equals(command)) {
            String operator = session.getUser().getName();
            String ip = session.getClientAddress().getAddress().getHostAddress();
            syncLogService.addLog(
                    SyncLogService.DELETE,
                    argument,
                    operator,
                    ip,
                    OperationLevel.IMPORTANT
            );
        }
        return super.afterCommand(session, request, reply);
    }
}