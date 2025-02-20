package com.muyangyo.filesyncclouddisk.syncCore.client.LinkCore;


import com.muyangyo.filesyncclouddisk.common.utils.DeviceIdGenerator;
import com.muyangyo.filesyncclouddisk.syncCore.server.LinkCore.DiscoveryServer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import static com.muyangyo.filesyncclouddisk.syncCore.client.ClientMain.*;


@Slf4j
public class DeviceExplorer {
    public static final String FIRST_REQUEST_STRING = "DISCOVER_DEVICE_REQUEST";

    /**
     * 寻找服务端(需要在线程中调用) <br>
     * 1. 尝试连接公网服务端 <br>
     * 2. 公网不可达，尝试局域网发现 <br>
     * 3.1 如果找到服务端会启动文件同步线程 <br>
     * 3.2. 未找到服务端，将在5分钟后重试 <br>
     */
    @SneakyThrows
    public static String connectToServer() {
        int retryCount = 0; // 重试次数
        long initialDelay = 5000; // 初始延迟时间（5秒）
        long maxDelay = 30 * 60 * 1000; // 最大延迟时间（30分钟）
        long step = 10000; // 每次增加的步长（10秒）
        long delay = initialDelay; // 当前延迟时间

        while (true) {
            // 尝试连接服务端
            boolean connected = false;
            String serverIP = null; // 最终的服务端IP

            // 步骤1：尝试公网连接 如果有公网服务端IP，则尝试连接
            if (!PUBLIC_SERVER_IP.isEmpty()) {
                log.info("尝试连接公网服务端 started");
                String serverDeviceId = DeviceExplorer.pingToPublicServer(PUBLIC_SERVER_IP, PUBLIC_SERVER_UDP_PORT);
                if (serverDeviceId != null) {
                    log.info("成功发现公网服务端设备,设备ID: [{}],确认中...", serverDeviceId);
                    connected = checkServerDeviceId(serverDeviceId);
                    if (connected) {
                        serverIP = PUBLIC_SERVER_IP;
                    }
                }
                log.info("尝试连接公网服务端结束 ended");
            }


            // 步骤2：公网不可达，尝试局域网发现
            if (!connected) {
                log.info("公网服务端不可达，尝试局域网发现 started");
                List<String> devices = DeviceExplorer.pingToLocalNetworkDevicesToDiscover(PUBLIC_SERVER_UDP_PORT);
                for (String deviceInfo : devices) { //有设备在局域网返回了服务端信息

                    String[] parts = deviceInfo.split("@");
                    String deviceId = parts[0];
                    String ip = parts[1];

                    // 判断是否是有效的服务端设备
                    if (checkServerDeviceId(deviceId)) {
                        log.info("发现本地服务端设备 [{}@{}]", deviceId, ip);
                        serverIP = ip;
                        connected = true;
                        break;
                    }

                }
                log.info("尝试局域网发现结束 ended");
            }

            // 连接成功后的逻辑
            if (connected) {
                return serverIP;
            } else {
                retryCount++;
                log.warn("未找到服务端，将在 {} 秒后重试... (重试次数: {})", delay / 1000.0, retryCount);
                Thread.sleep(delay);

                // 更新延迟时间（固定步长增加）
                delay = Math.min(delay + step, maxDelay); // 延迟时间增加固定步长，但不超过最大延迟时间
                log.info("第 {} 次重试", retryCount);
            }
        }
    }

    /**
     * 返回本地可能存在的设备列表
     *
     * @param port UDP端口
     * @return 设备列表
     */
    private static List<String> pingToLocalNetworkDevicesToDiscover(int port) {
        List<String> devices = new ArrayList<>();
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);// 开启广播模式
            byte[] requestData = FIRST_REQUEST_STRING.getBytes();
            // 1.发送请求
            log.info("通过UDP广播发送请求寻找本地服务器...");
            DatagramPacket requestPacket = new DatagramPacket(
                    requestData,
                    requestData.length,
                    InetAddress.getByName("255.255.255.255"),
                    port
            );
            socket.send(requestPacket);

            // 2.等待响应（3秒）
            socket.setSoTimeout(3000);
            byte[] buffer = new byte[512];

            //3. 接收响应
            //可能会有多个设备响应,所以循环接收,直到 3s 后没有响应
            while (true) {
                try {
                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(responsePacket); //阻塞等待接收数据
                    String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
                    log.info("收到响应：" + response);
                    if (response.startsWith(DiscoveryServer.RESPONSE_PREFIX)) {
                        String deviceId = response.split(":")[1]; // 解析响应获取设备修饰过的ID
                        deviceId = DeviceIdGenerator.getOriginalDeviceId(deviceId); // 还原设备ID
                        devices.add(deviceId + "@" + responsePacket.getAddress().getHostAddress()); // 设备ID和IP地址组成设备信息
                        log.info("发现设备：" + deviceId + "@" + responsePacket.getAddress().getHostAddress());
                    }
                } catch (java.net.SocketTimeoutException e) {
                    // 正常异常,超时则结束
                    break; // 总共广播时间为 3 s ,超时则结束,如果找到了则devices列表中会有相应的设备,没有则为空列表
                }
            }
        } catch (Exception e) {
            //其他异常
            log.error("发现设备失败,错误信息 [{}]", e.getMessage());
        }
        if (devices.isEmpty()) {
            log.warn("未在本地网络发现设备!");
        }
        return devices;
    }

    /**
     * 连接指定的公网服务器
     *
     * @param serverIp   服务器IP
     * @param serverPort 服务器端口
     * @return 连接成功返回deviceId, 否则返回null
     */
    private static String pingToPublicServer(String serverIp, int serverPort) {
        if (serverIp == null || serverPort <= 0) {
            log.error("公网服务器IP或端口错误");
            return null;
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            // 1. 发送请求
            log.info("连接公网服务器 [{}:{}]", serverIp, serverPort);
            DatagramPacket requestPacket = new DatagramPacket(
                    FIRST_REQUEST_STRING.getBytes(), 0,
                    FIRST_REQUEST_STRING.getBytes().length,
                    new InetSocketAddress(serverIp, serverPort)
            );
            socket.send(requestPacket);

            // 2. 等待响应
            socket.setSoTimeout(3000); // 3秒超时
            byte[] buffer = new byte[512];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket); // 阻塞等待接收数据
            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            log.info("收到公网服务器响应 [{}]", response);
            if (response.startsWith(DiscoveryServer.RESPONSE_PREFIX)) {
                String deviceId = response.split(":")[1]; // 解析响应获取设备修饰过的ID
                log.info("正确响应,结束发现进程");
                return DeviceIdGenerator.getOriginalDeviceId(deviceId); // 还原设备ID
            } else {
                log.error("连接公网失败，响应错误 [{}]", response);
                return null;
            }
        } catch (java.net.SocketTimeoutException e) {
            // 正常异常
            log.error("连接公网服务器超时,请检查网络连接或公网服务器是否正常运行");
            return null;
        } catch (Exception e) {
            // 其他异常
            log.error("连接公网服务器失败,错误信息 [{}]", e.getMessage());
            return null;
        }
    }

    private static boolean checkServerDeviceId(String deviceId) {
        if (deviceId.equals(SAVED_SERVER_DEVICE_ORIGIN_ID)) {
            log.info("确认服务端设备ID正确");
            return true;
        }
        log.warn("确认服务端设备ID错误, 期望: [{}] , 实际: [{}]", SAVED_SERVER_DEVICE_ORIGIN_ID, deviceId);
        return false;
    }
}