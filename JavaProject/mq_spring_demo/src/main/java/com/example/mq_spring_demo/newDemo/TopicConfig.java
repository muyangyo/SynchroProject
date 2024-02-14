package com.example.mq_spring_demo.newDemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

//@Configuration
public class TopicConfig {


    @Bean
    public Queue topic1() {
        return new Queue("topic1"); //默认 队列是 持久的, 非独占的, 非自动删除的
    }

    @Bean
    public Queue topic2() {
        return new Queue("topic2"); //默认 队列是 持久的, 非独占的, 非自动删除的
    }

    @Bean
    public Queue topic3() {
        return new Queue("topic3"); //默认 队列是 持久的, 非独占的, 非自动删除的
    }

    @Bean
    public Queue topic4() {
        return new Queue("topic4"); //默认 队列是 持久的, 非独占的, 非自动删除的
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic_test");
    }

    @Bean
    public Binding binding1(Queue topic1, TopicExchange topicExchange) {
        //return new Binding(topic1, topicExchange, "a.#"); 这种方式被舍弃了
        //只能采用 fluent 风格的API了
        return BindingBuilder.bind(topic1).to(topicExchange).with("a.*");
    }

    @Bean
    public Binding binding2(Queue topic2, TopicExchange topicExchange) {
        return BindingBuilder.bind(topic2).to(topicExchange).with("a.*");
    }

    @Bean
    public Binding binding3(Queue topic3, TopicExchange topicExchange) {
        return BindingBuilder.bind(topic3).to(topicExchange).with("*.a");
    }

    @Bean
    public Binding binding4(Queue topic4, TopicExchange topicExchange) {
        return BindingBuilder.bind(topic4).to(topicExchange).with("#.a");
    }

}

