package com.muyangyo.filesyncclouddisk.common.utils;

import com.muyangyo.filesyncclouddisk.common.exception.CanNotGetLocalIP;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    /**
     * 获取服务器的局域网IP地址
     *
     * @return 服务器的局域网IP地址，如果没有找到则返回null
     */
    public static String getServerIP() {
        try {
            // 获取所有网络接口
            for (Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                 networkInterfaces.hasMoreElements(); ) {

                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // 获取该网络接口的所有InetAddress
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                     inetAddresses.hasMoreElements(); ) {

                    InetAddress inetAddress = inetAddresses.nextElement();

                    // 检查该地址是否是局域网地址且不是回环地址
                    if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()) {
                        // 返回符合条件的IP地址
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            // 处理Socket异常，打印堆栈跟踪
            e.printStackTrace();
        }
        // 如果没有找到合适的IP地址，返回null
        throw new CanNotGetLocalIP("获取服务器的局域网IP地址失败");
    }
}

