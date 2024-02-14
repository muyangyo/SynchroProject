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
 * Date: 2024/2/13
 * Time: 15:04
 */
public class DirectProducer {
    private static final String EXCHANGE_NAME = "DirectExchangeDemo1";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQTool.getChannel();

        //执行逻辑
        {
            //声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, true, null);
            //声明绑定的队列1
            channel.queueDeclare("queueName3", false, false, false, null);
            channel.queueBind("queueName3", EXCHANGE_NAME, "info");

            //声明绑定的队列2
            channel.queueDeclare("queueName4", false, false, false, null);
            channel.queueBind("queueName4", EXCHANGE_NAME, "error");

            //发送消息
            String message = "你好";
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes(StandardCharsets.UTF_8));
        }

        RabbitMQTool.close(channel);
    }
}
