package demo1;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * Common.User: 沐阳Yo
 * Date: 2024/2/11
 * Time: 22:28
 */
public class Producer {
    public static final String QUEUE_NAME = "demo1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入密码:");
        String password = scanner.nextLine();

        //创建一个连接工厂(和JDBC的创建数据源类似)
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("43.139.85.203");//注意不需要端口号
        factory.setUsername("admin");
        factory.setPassword(password);

        //创建连接(就是JDBC的连接)
        Connection connection = factory.newConnection();

        //创建一个频道(这个是比JDBC多的一步)
        Channel channel = connection.createChannel();

        //进行操作
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);//声明队列
        String msg = "你好!";
        channel.basicPublish("", QUEUE_NAME, null, msg.getBytes(StandardCharsets.UTF_8));//发布消息


        //关闭资源
        channel.close();
        connection.close();
    }
}
