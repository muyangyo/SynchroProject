package com.muyang.mq.common.network.Requests;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/27
 * Time: 20:52
 */
@Data
public class QueueDeclareRequestArguments extends BasicRequestArguments implements Serializable {
    String queueName;
    boolean durable;
    boolean exclusive;
    boolean autoDelete;
    Map<String, Object> arguments;
}
