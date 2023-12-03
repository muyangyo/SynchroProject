package com.example.aop.model;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 21:12
 */
public enum ResultCode {
    ERROR(-1), SUCCESS(1);
    private int code;

    ResultCode(int code) {
        this.code = code;
    }
}
