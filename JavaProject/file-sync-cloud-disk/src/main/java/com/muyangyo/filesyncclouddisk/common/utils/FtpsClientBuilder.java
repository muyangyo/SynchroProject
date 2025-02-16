package com.muyangyo.filesyncclouddisk.common.utils;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;

import java.io.IOException;

public class FtpsClientBuilder {
    private static final int DEFAULT_TIMEOUT = 10000; // 10s

    public static FTPSClient build(String server, int port, String username, String password) throws IOException {
        // 创建支持隐式SSL的FTPS客户端
        FTPSClient ftpClient = new FTPSClient(true);
        ftpClient.setControlEncoding("UTF-8"); // 设置编码格式,必须在connect之前设置

        // 配置SSL参数
        ftpClient.setNeedClientAuth(false);
        ftpClient.setAuthValue("SSL");
        ftpClient.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());// 信任所有证书

        // 设置连接超时
        ftpClient.setConnectTimeout(DEFAULT_TIMEOUT);

        // 连接服务器（隐式FTPS）
        ftpClient.connect(server, port);

        // 登录
        if (!ftpClient.login(username, password)) {
            throw new RuntimeException("登录失败");
        }

        // 设置传输参数
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE); // 设置文件类型为二进制
        ftpClient.enterLocalPassiveMode(); // 进入被动模式,避免客户端连接失败
        ftpClient.execPBSZ(0);  // 设置保护缓冲区大小
        ftpClient.execPROT("P"); // 启用数据通道保护

        return ftpClient;
    }
}