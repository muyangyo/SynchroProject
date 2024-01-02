package com.muyang.mq.server.brokercore;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 13:54
 */
@Data
public class Binding {
    private String exchangeName;
    private String queueName;
    private String bindingKey;//为了Topic交换机而存在的,作为出题人
}
