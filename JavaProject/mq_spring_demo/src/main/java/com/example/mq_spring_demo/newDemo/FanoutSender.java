package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@Slf4j
//@RequestMapping("FanoutSender")
public class FanoutSender {
    @Autowired
    RabbitTemplate rabbitTemplate;


    @RequestMapping("fanoutSender")
    public void fanout(String content) {
        log.info("fanout开始广播数据：{}", content);
        rabbitTemplate.convertAndSend("fanout_test", "", content);
    }
}
