package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
//@Component
public class FanoutReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fanout1"), //没有也会自动声明
            exchange = @Exchange("fanout_test")
    ))
    public void fanoutReceiver2(String content) {
        log.info("广播模式1开始消费：{}", content);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("fanout2"), //没有也会自动声明
            exchange = @Exchange("fanout_test") //没有也会自动声明
    ))
    public void fanoutReceiver1(String content) {
        log.info("广播模式2开始消费：{}", content);
    }
}

