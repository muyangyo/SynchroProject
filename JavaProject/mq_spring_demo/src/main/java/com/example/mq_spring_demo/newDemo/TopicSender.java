package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
//
@Slf4j
//@RestController
//@RequestMapping("/TopicSender")
public class TopicSender {
    @Autowired
    private RabbitTemplate amqpTemplate;

    @GetMapping("/topicSender")
    public void toipc(String content, String RoutingKey) {
        log.info("通配符模式开始发布消息:{},RoutingKey:{}", content, RoutingKey);
        amqpTemplate.convertAndSend("topic_test", RoutingKey, content);
    }

}
