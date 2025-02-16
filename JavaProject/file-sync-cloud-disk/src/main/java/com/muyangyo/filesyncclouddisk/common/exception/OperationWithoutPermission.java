package com.muyangyo.filesyncclouddisk.common.exception;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/2
 * Time: 14:54
 */
public class OperationWithoutPermission extends RuntimeException {
    public OperationWithoutPermission(String message) {
        super(message);
    }
}
