package com.example.mq_spring_demo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/14
 * Time: 10:21
 */
@SpringBootTest
public class HighProducer {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void SendMessageToDelay() {

        rabbitTemplate.convertAndSend("DemoDelayExchange", "", "有延迟时间的消息", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                System.out.println("消息已经发送成功!发送时间为:" + new Date(System.currentTimeMillis()));
                message.getMessageProperties().setDelay(30 * 1000);
                return message;//必须返回加工好的信息
            }
        });

    }

    @Test
    public void SendMessageToLimit() {

        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("ExchangeDemo", "queue", "消息" + i, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    message.getMessageProperties().setExpiration(Integer.toString(3 * 1000));
                    return message;
                }
            });
        }

    }


}
