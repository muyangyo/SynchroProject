package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

//@Component //必须交给Spring管理
@Slf4j
public class SimpleReceiver {


    //监听 simple 队列，如果没有此队列则会先创建 返回值必须void
    @RabbitListener(queuesToDeclare = @Queue("simple"))
    public void SimpleReceiver(String content) {
        log.info("开始消费简单队列消息:{}", content);
    }

}