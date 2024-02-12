package Confirm;

import Common.RabbitMQTool;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * Common.User: 沐阳Yo
 * Date: 2024/2/11
 * Time: 22:29
 */
public class Consumer {
    public static final String QUEUE_NAME = "demo1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQTool.getChannel();

        //进行操作
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("消息为: " + msg);
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };//正常得到消息时执行

        CancelCallback cancelCallback = s -> System.out.println("消费消息中断!");//没有成功消费消息时执行
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);//订阅消息

        // 这里不用关闭资源,因为要一直等消息
    }
}
