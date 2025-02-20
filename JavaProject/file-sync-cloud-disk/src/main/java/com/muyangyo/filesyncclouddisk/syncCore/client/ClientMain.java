package com.muyangyo.filesyncclouddisk.syncCore.client;


import com.muyangyo.filesyncclouddisk.common.utils.DeviceIdGenerator;
import com.muyangyo.filesyncclouddisk.syncCore.client.FileProcessingCore.FileSyncManager;
import com.muyangyo.filesyncclouddisk.syncCore.client.LinkCore.DeviceExplorer;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.Sync;
import com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

import static com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain.FTPS_SERVER_PORT;

@Slf4j
public class ClientMain {
    // TODO: 这里应该有个配置文件,分别配置 公网服务端IP(数据库) 和 本地存储的服务端设备ID(数据库) 和 公网服务端端口(yml)
    public static final String SAVED_SERVER_DEVICE_ORIGIN_ID = "3413E8D57C17"; // 本地存储的服务端设备ID
    public static final String PUBLIC_SERVER_IP = "11.11.11.11"; // 公网服务端IP
    public static final int PUBLIC_SERVER_UDP_PORT = ServerMain.DISCOVERY_SERVER_PORT;//发现端口要一致

    public static final int FTP_PORT = FTPS_SERVER_PORT;

    public static void main(String[] args) {
        //todo:这个写进配置文件中
        String clientDeviceId = DeviceIdGenerator.generateDeviceId();
        log.info("客户端设备ID: " + clientDeviceId);

        // 每启动一次main就等于一次请求(后续会集成到springboot中)
        new Thread(() -> {
            String serverIp = DeviceExplorer.connectToServer();
            if (StringUtils.hasLength(serverIp)) {
                log.info("成功连接到服务器 [{}] ,执行文件同步操作", serverIp);
                // todo: 查询数据库文件
                ArrayList<Sync> syncs = new ArrayList<>();
//                syncs.add(new Sync("C:/Users/MuYang/Desktop/local - 副本", true, "123", "123456"));
                syncs.add(new Sync("C:/Users/MuYang/Desktop/local", false, "abc", "123456"));
                // end todo
                FileSyncManager fileSyncManager = new FileSyncManager(serverIp, FTP_PORT, syncs);
            }
        }).start();

        log.info("主线程结束!"); // todo: 模拟的返回响应
    }
}