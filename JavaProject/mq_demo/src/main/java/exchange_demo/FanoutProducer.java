package exchange_demo;

import Common.RabbitMQTool;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/12
 * Time: 22:53
 */
public class FanoutProducer {
    private static final String EXCHANGE_NAME = "FanoutExchangeDemo1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQTool.getChannel();

        //执行逻辑
        {
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, false, true, null);
            //声明绑定的队列1
            channel.queueDeclare("queueName1", false, false, false, null);
            channel.queueBind("queueName1", EXCHANGE_NAME, "");

            //声明绑定的队列2
            channel.queueDeclare("queueName2", false, false, false, null);
            channel.queueBind("queueName2", EXCHANGE_NAME, "");

            //发送消息
            String message = "你好";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
        }

        RabbitMQTool.close(channel);
    }
}
