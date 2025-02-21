package com.muyangyo.filesyncclouddisk.syncCore.server;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServerMainDemo {


/*    public static final int DISCOVERY_SERVER_PORT = 8888;    // warn: 后续这里使用配置文件读取端口号
    public static final int FTPS_SERVER_PORT = 9999;         // warn: 后续这里使用配置文件读取端口号
    public static final String KEYSTORE_FILE_NAME = "keystore.jks";// warn: 后续这里使用配置文件读取文件名
    public static final String KEYSTORE_PASSWORD = "123456";// warn: 后续这里使用配置文件读取密码
    public static final String VERSION_DIR = "./.versions_robust_delete"; // 保留文件夹名称 warn: 后续这里使用配置文件读取文件夹名称
    public static final int MAX_VERSIONS = 5; // 最大保留版本数 warn: 后续这里使用配置文件读取最大版本数
    public static final boolean ENABLE_VERSION_DELETE = true; // 是否开启版本删除功能 warn: 后续这里使用配置文件读取是否开启版本删除功能

    public static void main(String[] args) throws Exception {
        // 生成服务端设备ID（固定存储，避免重启变化） warn: 这里应该读取配置文件
        String serverDeviceId = DeviceIdGenerator.generateDeviceId();
        log.info("服务端设备ID: " + serverDeviceId);

        // 启动UDP广播监听
        DiscoveryServer discoveryServer = new DiscoveryServer(DISCOVERY_SERVER_PORT, serverDeviceId);
        new Thread(discoveryServer).start();

        // 启动FTPS Server
        if (!Files.exists(Paths.get("./" + KEYSTORE_FILE_NAME))) {
            // 自动生成 keystore.jks 文件
            log.info("{} 证书文件不存在正在自动生成...", KEYSTORE_FILE_NAME);
            if (autoGenerateKeystore(KEYSTORE_PASSWORD, KEYSTORE_FILE_NAME)) {
                log.info("{} 证书文件生成成功", KEYSTORE_FILE_NAME);
            } else {
                log.error("{} 证书文件生成失败", KEYSTORE_FILE_NAME);
                return;
            }
        }

        // 读取数据库
        ArrayList<FtpLoginUser> ftpLoginUsers = new ArrayList<>();
        ftpLoginUsers.add(new FtpLoginUser("123", "123456", "C:/Users/MuYang/Desktop/remote - 副本"));
        ftpLoginUsers.add(new FtpLoginUser("abc", "123456", "C:/Users/MuYang/Desktop/remote"));

        // 启动FTPS Server
        FTPsServer.run(KEYSTORE_PASSWORD,
                null,
                FTPS_SERVER_PORT,
                ftpLoginUsers
        );


        log.info("主线程结束");
    }*/
}