package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

@Slf4j
//@RestController
public class DirectSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/TemplateSender")
    public void directSender(String content, String RoutingKey) {
        log.info("开始生产消息：{},RoutingKey:{}", content, RoutingKey);
        rabbitTemplate.convertAndSend("direct_test", RoutingKey, content.getBytes(StandardCharsets.UTF_8));//自己手动指定编码格式更安全
    }
}
