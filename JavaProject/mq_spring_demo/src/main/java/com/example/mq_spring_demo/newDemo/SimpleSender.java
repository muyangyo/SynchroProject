package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Slf4j
//@RequestMapping("/SimpleSender")
public class SimpleSender {

    @Autowired
    private AmqpTemplate amqpTemplate; //内置对象

    @RequestMapping("/simple")
    public void simpleSend(String content) {
        log.info("简单队列发送消息：{}", content);
        this.amqpTemplate.convertAndSend("simple", content);//这里使用的默认交换机
    }


    @RequestMapping("workSender")
    public void workSender(String content) {
        log.info("work模式发送消息：{}", content);
        this.amqpTemplate.convertAndSend("work", content);
    }


}
