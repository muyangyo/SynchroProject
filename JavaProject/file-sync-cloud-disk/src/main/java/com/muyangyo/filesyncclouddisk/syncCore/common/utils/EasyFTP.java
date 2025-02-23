package com.muyangyo.filesyncclouddisk.syncCore.common.utils;

import cn.hutool.extra.ftp.Ftp;
import com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.customFtps.CRC32Command;
import com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.customFtps.VersionRemoveCommand;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/17
 * Time: 15:23
 */
// 对hutool-extra的Ftp进行了封装，增加了CRC32命令的支持,其他不变
public class EasyFTP extends Ftp {

    public EasyFTP(FTPClient client) {
        super(client);
    }

    public long getFileCRC32(String remotePath) throws IOException {
        FTPClient client = getClient(); // 获取FTPClient实例
        int code = client.sendCommand(CRC32Command.COMMAND_NAME, remotePath); // 发送CRC32命令
        String message = client.getReplyString(); // 获取命令回复信息
        if (FTPReply.isPositiveCompletion(code)) {
            String[] split = message.split("=|\\r");
            if (3 == split.length) {
                return Long.parseLong(split[1].trim());
            } else {
                throw new RuntimeException("CRC32命令回复信息格式错误，应为 [ CRC32=xxxx/r/n ]");
            }
        } else {
            throw new RuntimeException("执行获取CRC32命令失败，原因 [ " + message + " ]");
        }
    }

    public boolean versionRemoveFile(String remotePath) throws IOException {
        FTPClient client = getClient(); // 获取FTPClient实例
        int code = client.sendCommand(VersionRemoveCommand.COMMAND_NAME, remotePath); // 发送命令
        return FTPReply.isPositiveCompletion(code);
    }
}
