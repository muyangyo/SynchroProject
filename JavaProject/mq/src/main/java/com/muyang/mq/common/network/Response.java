package com.muyang.mq.common.network;

import lombok.Data;

/**
 * 响应结构
 */
@Data
public class Response {
    private int type;
    private int length;
    private byte[] payload;
}