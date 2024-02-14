package exchange_demo;

import Common.RabbitMQTool;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/13
 * Time: 15:57
 */
public class DeadExchangeProducer {
    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQTool.getChannel();

        //执行逻辑
        {
            //1. 声明死信和普通 交换机,类型为 DIRECT
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);

            //2. 声明 死信 队列
            String deadQueue = "dead_queue";
            channel.queueDeclare(deadQueue, false, false, false, null);
            channel.queueBind(deadQueue, DEAD_EXCHANGE, "deadMsg");


            //3. 先构造好 额外参数
            Map<String, Object> params = new HashMap<>();
            //设置 死信 交换机,参数 key 是固定的形式
            params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            //设置死信 RoutingKey,参数 key 是固定的形式
            params.put("x-dead-letter-routing-key", "deadMsg");

            //4. 声明 正常 队列
            String normalQueue = "normal_queue";
            channel.queueDeclare(normalQueue, false, false, false, params);//注意这里添加了额外参数
            channel.queueBind(normalQueue, NORMAL_EXCHANGE, "normal");

            //设置消息的 TTL 时间 10s
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration(Integer.toString(1000 * 10)).build();


            //5. 发送消息
            for (int i = 0; i < 10; i++) {
                String message = "info" + i;
                channel.basicPublish(NORMAL_EXCHANGE, "normal", properties, message.getBytes());
                System.out.println("生产者发送消息:" + message);
            }
        }

        RabbitMQTool.close(channel);
    }
}
