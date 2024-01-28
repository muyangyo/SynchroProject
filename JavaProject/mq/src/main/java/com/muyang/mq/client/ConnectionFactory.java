package com.muyang.mq.client;

import lombok.Data;

import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/28
 * Time: 20:59
 */

@Data
//工厂类
public class ConnectionFactory {
    // 要访问的 broker server 服务器的 ip 地址
    private String host;
    // 要访问的 broker server 服务器的 端口号
    private int port;

    public Connection newConnection() throws IOException {
        Connection connection = new Connection(host, port);
        return connection;
    }
}
