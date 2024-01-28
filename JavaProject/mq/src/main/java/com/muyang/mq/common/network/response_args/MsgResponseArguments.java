package com.muyang.mq.common.network.response_args;

import com.muyang.mq.server.brokercore.BasicProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/28
 * Time: 17:01
 */
@Data
public class MsgResponseArguments extends BasicResponseArguments implements Serializable {
    private String consumerTag;
    private BasicProperties basicProperties;
    private byte[] body;
}
