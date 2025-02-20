package com.muyangyo.filesyncclouddisk.syncCore.server;


import com.muyangyo.filesyncclouddisk.common.model.enums.SystemType;
import com.muyangyo.filesyncclouddisk.common.utils.DeviceIdGenerator;
import com.muyangyo.filesyncclouddisk.common.utils.OSUtils;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.FtpUser;
import com.muyangyo.filesyncclouddisk.syncCore.server.FileProcessingCore.ftps.FTPsServer;
import com.muyangyo.filesyncclouddisk.syncCore.server.LinkCore.DiscoveryServer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@Slf4j
public class ServerMain {


    public static final int DISCOVERY_SERVER_PORT = 8888;    // TODO: 后续这里使用配置文件读取端口号
    public static final int FTPS_SERVER_PORT = 9999;         // TODO: 后续这里使用配置文件读取端口号
    public static final String KEYSTORE_FILE_NAME = "keystore.jks";// TODO: 后续这里使用配置文件读取文件名
    public static final String KEYSTORE_PASSWORD = "123456";// TODO: 后续这里使用配置文件读取密码
    public static final String VERSION_DIR = "./.versions_robust_delete"; // 保留文件夹名称 TODO: 后续这里使用配置文件读取文件夹名称
    public static final int MAX_VERSIONS = 5; // 最大保留版本数 TODO: 后续这里使用配置文件读取最大版本数

    public static void main(String[] args) throws Exception {
        // 生成服务端设备ID（固定存储，避免重启变化） todo: 这里应该读取配置文件
        String serverDeviceId = DeviceIdGenerator.generateDeviceId();
        log.info("服务端设备ID: " + serverDeviceId);

        // 启动UDP广播监听
        DiscoveryServer discoveryServer = new DiscoveryServer(DISCOVERY_SERVER_PORT, serverDeviceId);
        new Thread(discoveryServer).start();

        // 启动FTPS Server
        if (!Files.exists(Paths.get("./" + KEYSTORE_FILE_NAME))) {
            // 自动生成 keystore.jks 文件
            log.info("{} 证书文件不存在正在自动生成...", KEYSTORE_FILE_NAME);
            if (autoGenerateKeystore(KEYSTORE_PASSWORD)) {
                log.info("{} 证书文件生成成功", KEYSTORE_FILE_NAME);
            } else {
                log.error("{} 证书文件生成失败", KEYSTORE_FILE_NAME);
                return;
            }
        }

        // TODO: 这里应该读取数据库
        ArrayList<FtpUser> ftpUsers = new ArrayList<>();
        ftpUsers.add(new FtpUser("123", "123456", "C:/Users/MuYang/Desktop/remote - 副本"));
        ftpUsers.add(new FtpUser("abc", "123456", "C:/Users/MuYang/Desktop/remote"));

        // 启动FTPS Server
        FTPsServer.run(KEYSTORE_PASSWORD,
                null,
                FTPS_SERVER_PORT,
                ftpUsers
        );


        log.info("主线程结束");
    }

    //todo: 这里应该读取配置文件,并需要移动位置
    public static boolean autoGenerateKeystore(String password) {
        try {
            // 1. 定义 keytool 命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "keytool", "-genkey", "-alias", "ftpserver", "-keyalg", "RSA",
                    "-keystore", "keystore.jks", "-keysize", "2048", "-validity", "3650",
                    "-storepass", password, "-keypass", password,
                    "-dname", "CN=demo, OU=IT, O=Owner, L=Beijing, ST=Beijing, C=CN"
            );

            // 2. 启动进程
            Process process = processBuilder.start();

            // 3. 读取命令输出
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), OSUtils.getSystemType() == SystemType.WINDOWS ? "GBK" : "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }


            // 4. 等待命令执行完成
            int exitCode = process.waitFor();

            // 5. 检查生成的 keystore.jks 文件
            File keystoreFile = new File(KEYSTORE_FILE_NAME);
            if (keystoreFile.exists()) {
                log.info("keystore.jks 文件已生成，路径：" + keystoreFile.getAbsolutePath());
                return true;
            } else {
                log.error("keystore.jks 文件未生成");
                return false;
            }
        } catch (Exception e) {
            log.error("自动生成 keystore.jks 文件失败", e);
            return false;
        }
    }
}