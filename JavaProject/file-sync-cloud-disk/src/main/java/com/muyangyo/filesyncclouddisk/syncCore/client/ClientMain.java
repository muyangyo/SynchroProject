package com.muyangyo.filesyncclouddisk.syncCore.client;


import com.muyangyo.filesyncclouddisk.common.utils.DeviceIdGenerator;
import com.muyangyo.filesyncclouddisk.syncCore.client.LinkCore.DeviceExplorer;
import com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMain {
    // TODO: 这里应该有个配置文件,分别配置 公网服务端IP(数据库) 和 本地存储的服务端设备ID(数据库) 和 公网服务端端口(yml)
    public static final String SAVED_SERVER_DEVICE_ORIGIN_ID = "3413E8D57C17"; // 本地存储的服务端设备ID
    public static final String PUBLIC_SERVER_IP = "11.11.11.11"; // 公网服务端IP
    public static final int PUBLIC_SERVER_UDP_PORT = ServerMain.DISCOVERY_SERVER_PORT;//发现端口要一致


    public static void main(String[] args) {
        //todo:这个写进配置文件中
        String clientDeviceId = DeviceIdGenerator.generateConfusedDeviceId();
        log.info("客户端设备ID: " + clientDeviceId);

        // 每启动一次main就等于一次请求(后续会集成到springboot中)
        new Thread(() -> {
            DeviceExplorer.connectToServer();
        }).start();

        log.info("主线程结束!"); // todo: 模拟的返回响应
    }

    //todo: 后续这个方法会移动到独立的类里面
    public static void startFileSync(String serverIp, int ftpPort) {
        // 执行文件同步逻辑(这里需要启动一个线程)
        log.info("连接FTPS服务端 [{}:{}]", serverIp, ftpPort);
        log.info("已连接服务端，文件同步线程启动...");
        // FileSyncManager.syncFiles();
    }
}