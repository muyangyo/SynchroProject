package com.muyang.mq.common.network.request_args;

import lombok.Data;

import java.io.Serializable;

@Data
public class BasicRequestArguments implements Serializable {
    // 表示一次 请求 的身份标识,用于把请求和响应对上
    protected String rid;
    // 这次通信使用的 channel 的身份标识
    protected String channelId;
}