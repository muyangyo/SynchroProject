package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Slf4j
//@Component
public class DirectReceiver {

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct1"),
            exchange = @Exchange("direct_test"),
            key = {"a"} //BindingKey,可以指定多个
    ))
    public void directReceiver1(String content) {
        log.info("路由模式1开始消费，{}", content);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct2"),
            exchange = @Exchange("direct_test"),
            key = {"b"} //BindingKey,可以指定多个
    ))
    public void directReceiver2(String content) {
        log.info("路由模式2开始消费，{}", content);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("direct3"),
            exchange = @Exchange("direct_test"),
            key = {"b"} //BindingKey,可以指定多个
    ))
    public void directReceiver3(String content) {
        log.info("路由模式3开始消费，{}", content);
    }
}

