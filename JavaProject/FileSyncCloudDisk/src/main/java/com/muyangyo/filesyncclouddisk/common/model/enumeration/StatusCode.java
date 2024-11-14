package com.muyangyo.filesyncclouddisk.common.model.enumeration;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/13
 * Time: 19:03
 */
public enum StatusCode {
    SUCCESS(200),
    FAILURE(400),
    ERROR(500);

    private int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
