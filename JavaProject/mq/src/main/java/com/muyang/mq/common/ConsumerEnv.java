package com.muyang.mq.common;

import lombok.Data;

/**
 * 真消费者(非回调函数)
 */
@Data
public class ConsumerEnv {
    private String consumerTag;
    private String queueName;
    private boolean autoAck;
    // 通过这个回调来处理收到的消息.
    private ConsumerBehavior consumerBehavior;

    public ConsumerEnv(String consumerTag, String queueName, boolean autoAck, ConsumerBehavior consumerBehavior) {
        this.consumerTag = consumerTag;
        this.queueName = queueName;
        this.autoAck = autoAck;
        this.consumerBehavior = consumerBehavior;
    }
}
