package com.muyangyo.fileclouddisk.common.exception;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/23
 * Time: 20:33
 */
public class IllegalLoginWithoutRSA extends RuntimeException {
    public IllegalLoginWithoutRSA(String message) {
        super(message);
    }
}
