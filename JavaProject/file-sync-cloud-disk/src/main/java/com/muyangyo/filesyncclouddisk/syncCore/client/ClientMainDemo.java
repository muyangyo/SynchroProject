package com.muyangyo.filesyncclouddisk.syncCore.client;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientMainDemo {
/*    // warn: 这里应该有个配置文件,分别配置 公网服务端IP(数据库) 和 本地存储的服务端设备ID(数据库) 和 公网服务端端口(yml)
    public static final String SAVED_SERVER_DEVICE_ORIGIN_ID = "3413E8D57C17"; // 本地存储的服务端设备ID
    public static final String PUBLIC_SERVER_IP = "11.11.11.11"; // 公网服务端IP
    public static final int PUBLIC_SERVER_UDP_PORT = ServerMainDemo.DISCOVERY_SERVER_PORT;//发现端口要一致

    public static final int FTP_PORT = FTPS_SERVER_PORT;

    public static void main(String[] args) {
        //warn:这个写进配置文件中
        String clientDeviceId = DeviceIdGenerator.generateDeviceId();
        log.info("客户端设备ID: " + clientDeviceId);

        // 每启动一次main就等于一次请求(后续会集成到springboot中)
        new Thread(() -> {
            String serverIp = DeviceExplorer.connectToServer();
            if (StringUtils.hasLength(serverIp)) {
                log.info("成功连接到服务器 [{}] ,执行文件同步操作", serverIp);
                // warn: 查询数据库文件
                ArrayList<SyncInfo> syncs = new ArrayList<>();
//                syncs.add(new SyncInfo("123", "123456", "C:/Users/MuYang/Desktop/local - 副本", true, null, null));
                syncs.add(new SyncInfo("abc", "123456", "C:/Users/MuYang/Desktop/local", false, null, null));
                // end warn
//                FileSyncManager fileSyncManager = new FileSyncManager(serverIp, FTP_PORT, syncs, setting.getSettingThreadPool());
            }
        }).start();

        log.info("主线程结束!");
    }*/
}