package com.muyangyo.filesyncclouddisk.syncCore.server.fileSyncProcessingCore.customCommand;

import com.muyangyo.filesyncclouddisk.common.utils.CRC32Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.command.AbstractCommand;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.impl.FtpIoSession;
import org.apache.ftpserver.impl.FtpServerContext;
import org.apache.ftpserver.impl.LocalizedFtpReply;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Slf4j
public class CRC32Command extends AbstractCommand {
    public static final String COMMAND_NAME = "CRC32"; //warning: 注意要避开原生FTP命令,可见于 CommandFactoryFactory.java 的静态代码块

    private static final int SYNTAX_ERROR = 501; // 语法错误

    private static final int SUCCESS = 200; // 命令执行成功

    private static final int FILE_NOT_FOUND = 550; // 文件不存在或读取失败

    /**
     * 构造响应函数
     *
     * @param code    响应代码
     * @param message 响应消息
     * @return 响应对象 LocalizedFtpReply <br>
     * 注意: 自定义FTP响应对象,不能使用 LocalizedFtpReply.translate 方法,因为里面有一个静态 Map<命令.状态,响应消息> 存储了所有 [命令.状态] 的响应消息,无法更改.
     */
    private static LocalizedFtpReply getResponse(int code, String message) {
        if (code == SUCCESS) {
            return new LocalizedFtpReply(code, message);
        } else if (code == FILE_NOT_FOUND) {
            return new LocalizedFtpReply(code, "requested action not taken");
        } else {
            return new LocalizedFtpReply(code, "Syntax error");
        }
    }

    @Override
    public void execute(FtpIoSession session, FtpServerContext context, FtpRequest request) throws IOException {
        session.resetState(); // warning: 必须重置状态,以便可以重复执行命令,否则会报错

        log.info("执行CRC32命令,参数为 [{}]", request.getArgument());
        String relativeFilePath = Paths.get(request.getArgument()).normalize().toString(); //从请求参数中获取文件路径

        // 校验参数是否为空
        if (!StringUtils.hasLength(relativeFilePath)) {
            log.error("CRC32命令参数为空");
            session.write(getResponse(SYNTAX_ERROR, null));
            return;
        }

        // 获取用户的主目录
        String homeDir = session.getUser().getHomeDirectory();
        log.info("用户主目录为 [{}]", homeDir);
        String fullPath = Paths.get(homeDir, relativeFilePath).toString();// 注意: 参数必须是 / 开头的相对路径,否则会报错(使用Paths修复了)
        log.info("CRC32命令计算文件路径为 [{}]", fullPath);

        try {
            long crc32Value = CRC32Util.calculateCRC32(new File(fullPath));
            log.info("文件 [{}] 的CRC32校验码为 [{}]", relativeFilePath, crc32Value);
            session.write(getResponse(SUCCESS, "CRC=" + crc32Value));
        } catch (IOException e) {
            // 如果文件不存在或读取失败，返回请求无法执行
            session.write(getResponse(FILE_NOT_FOUND, null));
            log.error("CRC32命令执行失败", e);
        }
    }

}