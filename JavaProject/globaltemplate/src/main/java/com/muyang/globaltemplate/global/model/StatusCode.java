package com.muyang.globaltemplate.global.model;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:16
 */
public enum StatusCode {
    SUCCESS(1), ERROR(-1);
    private final Integer code;

    StatusCode(Integer code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
