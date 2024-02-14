package com.example.mq_spring_demo.consumer;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/14
 * Time: 10:19
 */
@Slf4j
//@Component //必须要交给Spring管理才行

public class HighConsumer {


/*    @RabbitListener(bindings = {
            //创建绑定,如果队列和交换机不存在则声明新的队列
            @QueueBinding(
                    value = @Queue(arguments = {
//                            @Argument(name = "x-max-length", value = "3", type = "java.lang.Integer") //必须指定参数类型
                    }), //这里创建的是临时队列
                    exchange = @Exchange(value = "ExchangeDemo", type = "fanout", arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "deadDemoExchange"),
                            @Argument(name = "x-dead-letter-routing-key", value = "default"),
                    }),
                    key = {"queue"}
            )
    })
    public void demoExchangeConsumer(String message) throws InterruptedException {
        Thread.sleep(3000);
        log.info("测试交换机的一个临时队列已经收到了消息" + message);
    }


    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(value = "deadQueue"),
                    exchange = @Exchange(value = "deadDemoExchange", type = "direct"),
                    key = {"default"}
            )
    })
    public void demoDeadExchangeConsumer(String message) {
        log.info("死信交换机的一个默认队列已经收到了消息" + message);
    }

*//*
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange(value = "DemoDelayExchange", type = "x-delayed-message", arguments = {
                            @Argument(name = "x-delayed-type", value = "direct")
                    })
            )
    })
    public void demoDelayExchangeConsumer(String message) {
        log.info("延迟交换机的一个临时队列已经收到了消息" + message);
    }*/



}
