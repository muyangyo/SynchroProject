package demo2;

import Common.User;
import com.rabbitmq.client.*;

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
        //创建一个连接工厂(和JDBC的创建数据源类似)
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("43.139.85.203");
        factory.setUsername(User.NAME);
        factory.setPassword(User.PASSWORD);

        //创建连接(就是JDBC的连接)
        Connection connection = factory.newConnection();

        //创建一个频道(这个是比JDBC多的一步)
        Channel channel = connection.createChannel();

        //进行操作
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String msg = new String(delivery.getBody());
                System.out.println("消息为: " + msg);
            }
        };//正常得到消息时执行

        CancelCallback cancelCallback = s -> {
            System.out.println("消费消息中断!");
            try {
                channel.close();
                connection.close();
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        };//取消订阅时触发

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);//订阅消息


    /*//关闭资源
    channel.close();
    connection.close();*/
        // 这里不用关闭资源,因为要一直等消息
    }
}
