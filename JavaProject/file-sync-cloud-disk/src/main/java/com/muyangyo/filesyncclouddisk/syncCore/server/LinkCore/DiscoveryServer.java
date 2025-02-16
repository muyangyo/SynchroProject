package com.muyangyo.filesyncclouddisk.syncCore.server.LinkCore;

import com.muyangyo.filesyncclouddisk.syncCore.client.LinkCore.DeviceExplorer;
import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

@Slf4j
public class DiscoveryServer implements Runnable {
    private final int UDP_PORT;
    private final String deviceId;
    public final static String RESPONSE_PREFIX = "DISCOVER_DEVICE_RESPONSE:"; // 回复消息前缀

    public DiscoveryServer(int udpPort, String deviceId) {
        this.UDP_PORT = udpPort;
        this.deviceId = deviceId;
    }

    @Override
    public void run() {
        log.info("启动 UDP 发现设备服务,监听端口 [{}]", UDP_PORT);
        // 启动 UDP 发现设备服务
        try (DatagramSocket socket = new DatagramSocket(UDP_PORT)) {
            byte[] buffer = new byte[1024];
            while (true) {
                //1.等待请求
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket); // 接收客户端广播(会被阻塞)
                String request = new String(requestPacket.getData(), 0, requestPacket.getLength()); // 解析消息

                //2.处理请求
                if (DeviceExplorer.FIRST_REQUEST_STRING.equals(request)) {
                    log.info("收到客户端 [{}] 正确的请求,返回设备信息中", requestPacket.getAddress().getHostAddress());
                    // 3.回复设备信息
                    String response = RESPONSE_PREFIX + deviceId;
                    byte[] responseData = response.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket(
                            responseData,
                            responseData.length,
                            requestPacket.getSocketAddress()
                    );
                    socket.send(responsePacket);
                    log.info("已经回复设备信息,等待下一个发现请求");
                } else {
                    log.warn("收到客户端 [{}] 非法请求,但请求内容 [{}] 不是预期的 [{}]", requestPacket.getAddress().getHostAddress(), request, DeviceExplorer.FIRST_REQUEST_STRING);
                }
            }
        } catch (Exception e) {
            log.error(" UDP 发现设备服务异常", e);
        }
    }
}