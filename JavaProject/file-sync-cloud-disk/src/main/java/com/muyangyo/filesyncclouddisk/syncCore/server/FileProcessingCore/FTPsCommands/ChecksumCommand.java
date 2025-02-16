package com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.FTPsCommands;

import com.muyangyo.filesyncclouddisk.common.utils.ChecksumUtil;
import org.apache.ftpserver.command.AbstractCommand;
import org.apache.ftpserver.ftplet.DefaultFtpReply;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.impl.FtpIoSession;
import org.apache.ftpserver.impl.FtpServerContext;

import java.io.File;
import java.io.IOException;

public class ChecksumCommand extends AbstractCommand {

    @Override
    public void execute(FtpIoSession ftpIoSession, FtpServerContext ftpServerContext, FtpRequest ftpRequest) throws IOException, FtpException {
        String fileName = ftpRequest.getArgument(); // 获取文件名
        File file = new File(String.valueOf(ftpIoSession.getFileSystemView().getWorkingDirectory()), fileName); // 获取文件路径

        if (!file.exists()) {
            ftpIoSession.write(new DefaultFtpReply(550, "File not found")); //如果文件不存在，返回550
            return;
        }

        long checksum = ChecksumUtil.calculateCRC32(file); // 计算校验码
        ftpIoSession.write(new DefaultFtpReply(213, String.valueOf(checksum))); // 返回校验码
    }
}