package com.muyang.mq.common.network.response_args;

import lombok.Data;

import java.io.Serializable;


@Data
public class BasicResponseArguments implements Serializable {
    // 表示一次 响应 的身份标识,用于把请求和响应对上
    protected String rid;
    // 用来标识一个 channel
    protected String channelId;
    // 表示当前这个远程调用方法的返回值
    protected boolean ok;
}