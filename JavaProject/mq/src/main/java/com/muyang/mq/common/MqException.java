package com.muyang.mq.common;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/5
 * Time: 21:38
 */
// 自定义异常
public class MqException extends Exception {
    public MqException(String message) {
        super(message);
    }
}
