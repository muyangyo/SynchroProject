package com.example.mq_spring_demo.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/13
 * Time: 17:59
 */
@Slf4j
//@Component //必须要交给Spring管理才行
public class Consumer {

    // @RabbitListener(queuesToDeclare = @Queue(value = "queueName", durable = "true", autoDelete = "false", exclusive = "false"))
    // 默认的队列属性是 持久化 , 非独占 , 不自动删除


    @RabbitListener(bindings = {
            //创建绑定,如果队列和交换机不存在则声明新的队列
            @QueueBinding(
                    value = @Queue, //这里创建的是临时队列
                    exchange = @Exchange(value = "DirectExchangeDemo", type = "direct"),
                    key = {"direct_queue"}
            )
    })
    public void DirectExchangeConsumer(String message) {
        log.info("直接交换机的一个临时队列已经收到了消息" + message);
    }


    @RabbitListener(bindings = {
            //创建绑定,如果队列和交换机不存在则声明新的队列
            @QueueBinding(
                    value = @Queue, //这里创建的是临时队列
                    exchange = @Exchange(value = "FanoutExchangeDemo", type = "fanout"),
                    key = {}
            )
    })
    public void FanoutExchangeConsumer(String message) {
        log.info("扇出交换机的临时队列已经收到了消息" + message);
    }


    @RabbitListener(bindings = {
            //创建绑定,如果队列和交换机不存在则声明新的队列
            @QueueBinding(
                    value = @Queue, //这里创建的是临时队列
                    exchange = @Exchange(value = "TopicExchangeDemo", type = "topic"),
                    key = {"topic_queue.#", "*.a.*"}

            )
    })
    public void TopicExchangeConsumer(String message) {
        log.info("主题交换机的一个临时队列已经收到了消息" + message);
    }


}


