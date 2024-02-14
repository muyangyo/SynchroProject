package com.example.mq_spring_demo.newDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/14
 * Time: 20:09
 */
@Slf4j
@Configuration
public class Config implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct //由于属性注入比实例化对象晚,所以不能使用构造方法进行操作
    public void register() {
//        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * @param returnedMessage 被返回的消息(消息简单包装了下) <br>
     *                        具有以下参数 <br>
     *                        private final Message message; 消息本体<br>
     *                        private final int replyCode; 返回码<br>
     *                        private final String replyText; 返回原因<br>
     *                        private final String exchange; 返回前的交换机<br>
     *                        private final String routingKey; 返回前的RoutingKey<br>
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息内容为: {} 的消息被退回,退回原因是: {} ", returnedMessage.getMessage(), returnedMessage.getReplyText());
    }


    /**
     * 回调函数
     *
     * @param correlationData 持有 消息的ID 以及 相关信息 ,由程序员自己设置在消息里
     * @param isReceived      交换机是否成功收到消息,收到返回 ture,没收到返回 false
     * @param cause           失败的原因(成功发送则没有)
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean isReceived, String cause) {
        if (!isReceived) {
            log.info(correlationData.getId() + " 的消息发送失败!失败原因为: " + cause);//id里可以设置任意东西
        } else {
            log.info(correlationData.getId() + " 的消息发送成功!");
        }
    }


}
