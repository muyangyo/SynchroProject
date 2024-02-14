package com.example.mq_spring_demo;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/13
 * Time: 18:02
 */
@SpringBootTest //为了方便发送消息,所以加了这个,其实这些发送消息的逻辑应该在其他逻辑代码里
public class Producer {

    @Autowired
    private RabbitTemplate rabbitTemplate;//内置对象

    @Test //为了方便发送消息,所以加了这个,其实这些发送消息的逻辑应该在其他逻辑代码里
    public void SendToDirectExchange() {
        rabbitTemplate.convertAndSend("DirectExchangeDemo", "direct_queue", "消息1");
    }

    @Test //为了方便发送消息,所以加了这个,其实这些发送消息的逻辑应该在其他逻辑代码里
    public void SendToFanoutExchange() {
        rabbitTemplate.convertAndSend("FanoutExchangeDemo", "", "消息2", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration(Integer.toString(3000));

                return message;
            }
        });
    }

    @Test //为了方便发送消息,所以加了这个,其实这些发送消息的逻辑应该在其他逻辑代码里
    public void SendToTopicExchange() {

        rabbitTemplate.convertAndSend("TopicExchangeDemo",
                "topic_queue", "消息3");
    }


}
