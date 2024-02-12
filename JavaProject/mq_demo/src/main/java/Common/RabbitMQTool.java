package Common;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/12
 * Time: 21:11
 */
public class RabbitMQTool {
    private static HashMap<Channel, Connection> channelConnectionHashMap = new HashMap<>(5);

    public static Channel getChannel() throws IOException, TimeoutException {
        //创建一个连接工厂(和JDBC的创建数据源类似)
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("43.139.85.203");
        factory.setUsername(User.NAME);
        factory.setPassword(User.PASSWORD);

        //创建连接(就是JDBC的连接)
        Connection connection = factory.newConnection();

        //创建一个频道(这个是比JDBC多的一步)
        Channel channel = connection.createChannel();
        channelConnectionHashMap.put(channel, connection);
        return channel;
    }

    public static void close(Channel channel) throws IOException, TimeoutException {
        Connection connection = channelConnectionHashMap.get(channel);
        channel.close();
        connection.close();
    }

}
