package com.muyang.mq.common.network.request_args;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasicConsumeRequestArguments extends BasicRequestArguments implements Serializable {
    private String consumerTag;
    private String queueName;
    private boolean autoAck;
    // 这个类对应的 basicConsume 方法中, 还有一个参数是一个回调函数(ConsumerBehavior)
    // 而函数是没办法通过序列化进行传输的.再者,这个方法本来就是客户自定义的
    // 我们只要把 信息 穿过去(固定回调函数),具体怎么处理在客户端进行处理即可
}
