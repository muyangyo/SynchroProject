package com.example.syncdemo2502131.server.FileProcessingCore;

import com.example.syncdemo2502131.server.FileProcessingCore.model.TtpUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;

import java.io.File;

import static com.example.syncdemo2502131.server.ServerMain.KEYSTORE_FILE_NAME;

@Slf4j
public class ImplicitFtpsServer {
    public static void run(String keystorePassword, File keystoreFile, TtpUser ftpUser, int ftpPort) throws Exception {
        if (keystoreFile == null || !keystoreFile.exists()) {
            keystoreFile = new File("./" + KEYSTORE_FILE_NAME);
        }

        // 1. 创建 FTP 服务器工厂
        FtpServerFactory serverFactory = new FtpServerFactory();

        // 2. 配置 SSL/TLS
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        ssl.setKeystoreFile(keystoreFile); // 证书文件
        ssl.setKeystorePassword(keystorePassword); // 证书密码

        // 3. 配置监听器（隐式 FTPS）
        ListenerFactory listenerFactory = new ListenerFactory();// 创建监听器工厂
        listenerFactory.setPort(ftpPort); // 隐式 FTPS 默认端口
        listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
        listenerFactory.setImplicitSsl(true); // 启用隐式 FTPS

        serverFactory.addListener("default", listenerFactory.createListener()); // 添加监听器

        // 4. 配置用户
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        BaseUser user = new BaseUser();
        user.setName(ftpUser.getUsername());
        user.setPassword(ftpUser.getPassword());
        user.setHomeDirectory(ftpUser.getPath());
        user.setEnabled(true);
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        serverFactory.getUserManager().save(user); // 保存用户

        // 5. 启动服务器
        FtpServer server = serverFactory.createServer();
        server.start();
        log.info("FTPS服务器启动成功！端口 [{}]", ftpPort);
    }
}