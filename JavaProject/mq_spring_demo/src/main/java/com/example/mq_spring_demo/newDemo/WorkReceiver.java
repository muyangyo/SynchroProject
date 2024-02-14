package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.nio.channels.Channel;

@Slf4j
//@Component
//放在类上，监听到消息后会交给@RabbitHandler的方法进行处理，如果有多个方法,会根据参数类型进行选择
//@RabbitListener(queuesToDeclare = @Queue("work") )
public class WorkReceiver {

    @RabbitListener(queuesToDeclare = @Queue("work"))
    @RabbitHandler
    public void workReceiver1(String content) throws InterruptedException {
        log.info("work模式开始消费1：{}", content);
        Thread.sleep(1000);
    }

    @RabbitListener(queuesToDeclare = @Queue("work"))
    @RabbitHandler
    public void workReceiver2(String content, Channel channel) throws IOException, InterruptedException {
        log.info("work模式开始消费2：{}", content);
        Thread.sleep(2000);
    }
}
