package com.muyang.mq.server.brokercore;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 17:36
 */
@Data
public class BasicProperties {
    private String id;
    /*
    不同类型的交换机,意义不一样
    DIRECT 是为指定的队列名
    FANOUT 无意义
    TOPIC  是回答问题的
    */
    private String routingKey;

    private Integer deliverMode = 1;//持久化存储,1表示不持久化,2表示持久化

}
