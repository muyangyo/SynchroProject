package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/14
 * Time: 16:06
 */
@RestController
@Slf4j
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/TemplateSender")
    public void directSender(String content, String RoutingKey, String exchangeName) {
        for (int i = 0; i < 10; i++) {
            log.info("开始生产消息：{},RoutingKey:{}", content + i, RoutingKey);
            rabbitTemplate.convertAndSend(exchangeName, RoutingKey,
                    (content + i).getBytes(StandardCharsets.UTF_8), new CorrelationData(content + i));
        }

    }

    @RabbitListener(queues = "demoQueue1")
    public void receiver(String content) {
        log.info("延迟队列开始消费：{}", content);
    }

/*    @RabbitListener(queues = "deadDemoQueue")
    public void DeadQueueReceiver(String content) {
        log.info("死信队列开始消费：{}", content);
    }*/
}
