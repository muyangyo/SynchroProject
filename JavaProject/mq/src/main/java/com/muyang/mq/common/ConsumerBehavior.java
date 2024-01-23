package com.muyang.mq.common;

import com.muyang.mq.server.brokercore.BasicProperties;

import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/23
 * Time: 16:14
 */
@FunctionalInterface
public interface ConsumerBehavior {
    /**
     * 在每次服务器收到消息之后, 来调用(回调函数)
     */
    void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) throws MqException, IOException;
}
