package com.muyang.mq.server.brokercore.constant;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 13:55
 */
public enum ExchangeType {
    DIRECT(0), FANOUT(1), TOPIC(2);
    private final int type;

    ExchangeType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
