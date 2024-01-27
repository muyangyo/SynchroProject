package com.muyang.mq.common.network.Requests;

import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class ExchangeDeclareRequestArguments extends BasicRequestArguments implements Serializable {
    private String exchangeName;
    private ExchangeType exchangeType;
    private boolean durable;
    private boolean autoDelete;
    private Map<String, Object> arguments;
}
