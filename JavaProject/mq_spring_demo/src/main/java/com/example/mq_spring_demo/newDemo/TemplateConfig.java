package com.example.mq_spring_demo.newDemo;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/14
 * Time: 16:08
 */
@Configuration
public class TemplateConfig {

    //正常队列
    @Bean
    public DirectExchange demoExchange1() {
        return new DirectExchange("demoExchange1");
    }

    @Bean
    public Queue demoQueue1() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "deadDemoExchange");
        arguments.put("x-dead-letter-routing-key", "default");
        return new Queue("demoQueue1", false, false, false, arguments);
    }

    @Bean
    public Binding demoBinding1(Queue demoQueue1, DirectExchange demoExchange1) {
        return BindingBuilder.bind(demoQueue1).to(demoExchange1).with("key");
    }


    //创建死信逻辑
    @Bean
    public DirectExchange deadDemoExchange() {
        return new DirectExchange("deadDemoExchange");
    }

    @Bean
    public Queue deadDemoQueue() {
        return new Queue("deadDemoQueue", false, false, false);
    }

    @Bean
    public Binding deadBinding(Queue deadDemoQueue, DirectExchange deadDemoExchange) {
        return BindingBuilder.bind(deadDemoQueue).to(deadDemoExchange).with("default");
    }



/*    @Bean
    public CustomExchange delayDemoExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange("delayDemoExchange", "x-delayed-message", false, false, arguments);
    }

    @Bean
    public Queue delayDemoQueue() {
        return new Queue("delayDemoQueue", false, false, false);
    }

    @Bean
    public Binding delayBinding(Queue delayDemoQueue, CustomExchange delayDemoExchange) {
        return BindingBuilder.bind(delayDemoQueue).to(delayDemoExchange).with("key").noargs();
    }*/
}
