package com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.ftps;

import com.muyangyo.filesyncclouddisk.syncCore.common.model.FtpUser;
import com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.ftps.customCommand.CRC32Command;
import com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.ftps.customCommand.VersionRemoveCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.command.CommandFactoryFactory;
import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain.KEYSTORE_FILE_NAME;


@Slf4j
public class FTPsServer {
    public static void run(String keystorePassword, File keystoreFile, int ftpPort, List<FtpUser> ftpUsers) throws Exception {
        if (keystoreFile == null || !keystoreFile.exists()) {
            keystoreFile = new File("./" + KEYSTORE_FILE_NAME);
        }

        // 1. 创建 FTP 服务器工厂
        FtpServerFactory serverFactory = new FtpServerFactory();
        // 创建命令工厂
        CommandFactoryFactory commandFactoryFactory = new CommandFactoryFactory();
        commandFactoryFactory.addCommand(CRC32Command.COMMAND_NAME, new CRC32Command());// 添加自定义命令1
        commandFactoryFactory.addCommand(VersionRemoveCommand.COMMAND_NAME, new VersionRemoveCommand());// 添加自定义命令2
        serverFactory.setCommandFactory(commandFactoryFactory.createCommandFactory());


        // 2. 配置 SSL/TLS
        SslConfigurationFactory ssl = new SslConfigurationFactory();
        ssl.setKeystoreFile(keystoreFile); // 证书文件
        ssl.setKeystorePassword(keystorePassword); // 证书密码


        // 3. 配置监听器（隐式 FTPS）
        ListenerFactory listenerFactory = new ListenerFactory();// 创建监听器工厂
        listenerFactory.setPort(ftpPort); // 隐式 FTPS 端口
        listenerFactory.setSslConfiguration(ssl.createSslConfiguration());
        listenerFactory.setImplicitSsl(true); // 启用隐式 FTPS
        // 设置被动端口范围
        DataConnectionConfigurationFactory dataConfig = new DataConnectionConfigurationFactory();
        dataConfig.setPassivePorts("50000-50020");// 设置被动端口范围
        listenerFactory.setDataConnectionConfiguration(dataConfig.createDataConnectionConfiguration());

        serverFactory.addListener("default", listenerFactory.createListener()); // 添加监听器

        // 4. 配置用户
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        for (FtpUser ftpUser : ftpUsers) {
            BaseUser user = new BaseUser();
            user.setName(ftpUser.getUsername());
            user.setPassword(ftpUser.getPassword());
            user.setHomeDirectory(ftpUser.getPath());

            LinkedList<Authority> authorities = new LinkedList<>();
            authorities.add(new WritePermission());// 设置写权限
            authorities.add(new ConcurrentLoginPermission(1, 10));
            user.setAuthorities(authorities);

            user.setEnabled(true);
            serverFactory.getUserManager().save(user); // 保存用户
            log.info("用户 [{}] 注册成功，家目录: {}", ftpUser.getUsername(), ftpUser.getPath());
        }

        // 5. 启动服务器
        FtpServer server = serverFactory.createServer();
        server.start();
        log.info("FTPS服务器启动成功！端口 [{}]，已注册用户数: {}", ftpPort, ftpUsers.size());
    }
}