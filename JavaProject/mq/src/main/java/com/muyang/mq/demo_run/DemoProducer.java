package com.muyang.mq.demo_run;

import com.muyang.mq.client.Channel;
import com.muyang.mq.client.Connection;
import com.muyang.mq.client.ConnectionFactory;
import com.muyang.mq.server.brokercore.constant.ExchangeType;

import java.io.IOException;

/*
 * 这个类用来表示一个生产者.
 * 通常这是一个单独的服务器程序.
 */
public class DemoProducer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("启动生产者");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(9090);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 创建交换机和队列
        channel.exchangeDeclare("testExchange", ExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("testQueue", true, false, false, null);

        // 创建一个消息并发送
        byte[] body = "hello1".getBytes();
        boolean ok = channel.basicPublish("testExchange", "testQueue", null, body);
        System.out.println("消息投递完成! ok=" + ok);

        Thread.sleep(500);
        channel.close();
        connection.close();
    }
}
