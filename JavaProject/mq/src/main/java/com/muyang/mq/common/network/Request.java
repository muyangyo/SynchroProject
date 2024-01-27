package com.muyang.mq.common.network;

import lombok.Data;

/**
 * 请求结构
 */
@Data
public class Request {
    private int type;
    private int length;//载荷长度
    private byte[] payload;
}