package com.muyang.mq.common.network;

import lombok.Getter;
import lombok.Setter;

/**
 * 响应结构
 */
@Getter
@Setter
public class Response {
    private int type;
    private int length;//载荷长度
    private byte[] payload;

    @Override
    public String toString() {
        return "Response(" +
                "type=" + type +
                ", length=" + length +
                ')';
    }
}