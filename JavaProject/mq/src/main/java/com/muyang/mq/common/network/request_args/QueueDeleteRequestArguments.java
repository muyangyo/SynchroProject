package com.muyang.mq.common.network.request_args;

import lombok.Data;

import java.io.Serializable;



/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/27
 * Time: 20:54
 */
@Data

public class QueueDeleteRequestArguments extends BasicRequestArguments implements Serializable {
    String queueName;
}
