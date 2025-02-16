package com.muyangyo.filesyncclouddisk.common.utils;


import com.muyangyo.filesyncclouddisk.common.exception.CanNotGetLocalIP;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Slf4j
public class NetworkUtils {

    @SneakyThrows // 在前端有端口映射时无效
    public static boolean isLocalhost(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        InetAddress inetAddress = InetAddress.getByName(remoteAddr);
        return inetAddress.isLoopbackAddress();
    }

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
            log.error("获取服务器的局域网IP地址失败", e);
        }
        // 如果没有找到合适的IP地址，返回null
        throw new CanNotGetLocalIP("获取服务器的局域网IP地址失败");
    }

    /**
     * 获取客户端的真实IP地址
     *
     * @param request HTTP请求
     * @return 客户端的真实IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 如果通过了多级代理，X-Forwarded-For的值会是多个IP地址，第一个为真实IP地址
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0];
        }
        return ip;
    }
}

