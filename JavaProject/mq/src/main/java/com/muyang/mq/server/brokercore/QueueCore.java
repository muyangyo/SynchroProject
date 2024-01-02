package com.muyang.mq.server.brokercore;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 13:52
 */
@Data
public class QueueCore {
    private String name;//队列名称
    private Boolean durable = false;//是否持久化
    private Boolean exclusive = false;//是否为专属(对消费者而言)
    private Boolean autoDelete = false;//是否自动删除
    private Map<String, Objects> args = new HashMap<>();//额外参数

}
