package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
//@Component
//@Configuration
public class TopicReceiver {

    @RabbitListener(queues = "topic1")
    public void topicReceiver1(String content) {
        log.info("通配符模式1开始订阅：{}", content);
    }


    @RabbitListener(queues = {"topic2", "topic4"})
    public void topicReceiver2(String content) {
        log.info("通配符模式2开始订阅：{}", content);
    }


    @RabbitListener(queues = "topic3")
    public void topicReceiver3(String content) {
        log.info("通配符模式3开始订阅：{}", content);
    }

    @RabbitListener(queues = "topic4")
    public void topicReceiver4(String content) {
        log.info("通配符模式4开始订阅：{}", content);
    }
}

