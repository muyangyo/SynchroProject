package com.muyang.mq.common.network.Requests;

import com.muyang.mq.server.brokercore.BasicProperties;
import lombok.Data;

import java.io.Serializable;

@Data
public class BasicPublishRequestArguments extends BasicRequestArguments implements Serializable {
    private String exchangeName;
    private String routingKey;
    private BasicProperties basicProperties;
    private byte[] body;
}
