package com.muyang.mq.server.brokercore;

import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.Data;

import java.lang.ref.PhantomReference;
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
public class Exchange {
    private String name;//交换机名称
    private ExchangeType type = ExchangeType.DIRECT;//交换机类型,默认Direct
    private Boolean durable = false;//是否持久化存储
    private Boolean autoDelete = false;//是否自动删除
    private Map<String, Objects> args = new HashMap<>();//额外的参数选项
}
