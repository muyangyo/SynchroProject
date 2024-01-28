package com.muyang.mq.common.network.request_args;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueueUnbindRequestArguments extends BasicRequestArguments implements Serializable {
    private String queueName;
    private String exchangeName;
}
