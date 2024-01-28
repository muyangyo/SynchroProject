package com.muyang.mq.demo_run;

import com.muyang.mq.client.Channel;
import com.muyang.mq.client.Connection;
import com.muyang.mq.client.ConnectionFactory;
import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.BasicProperties;
import com.muyang.mq.server.brokercore.constant.ExchangeType;

import java.io.IOException;

/*
 * 这个类表示一个消费者.
 * 通常这个类也应该是在一个独立的服务器中被执行
 */
public class DemoConsumer {
    public static void main(String[] args) throws IOException, MqException, InterruptedException {
        System.out.println("启动消费者!");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(9090);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare("testExchange", ExchangeType.DIRECT, true, false, null);
        channel.queueDeclare("testQueue", true, false, false, null);

        channel.basicConsume("testQueue", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) throws MqException, IOException {
                System.out.println("[消费数据] 开始!");
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("basicProperties=" + basicProperties);
                String bodyString = new String(body, 0, body.length);
                System.out.println("body=" + new String(body));
                System.out.println("[消费数据] 结束!");
            }
        });


        // 由于消费者也不知道生产者要生产多少, 就在这里通过这个循环模拟一直等待消费.
//        while (true) {
//            Thread.sleep(500);
//        }
        System.out.println("主线程已完成,其他线程处理信息中...");
    }
}
