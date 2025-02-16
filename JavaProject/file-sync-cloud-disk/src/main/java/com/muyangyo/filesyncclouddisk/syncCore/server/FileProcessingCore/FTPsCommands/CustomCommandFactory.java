package com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.FTPsCommands;

import org.apache.ftpserver.command.Command;
import org.apache.ftpserver.command.CommandFactory;

public class CustomCommandFactory implements CommandFactory {

    private static final String GET_META_DATA_SHA1_COMMAND = "XSHA1"; // 获取文件元数据的SHA1校验码指令

    @Override
    public Command getCommand(String commandName) {
        // 确认命令是否为自定义命令
        if (GET_META_DATA_SHA1_COMMAND.equalsIgnoreCase(commandName)) {
            return new ChecksumCommand(); // 返回自定义命令对象处理
        }
        return null;// 自定义命令不存在，返回null
    }
}
