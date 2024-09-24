package com.muyangyo.wechatsmallprogram.global.Component;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:17
 */
@Data
public class Result {
    private Boolean success;
    private String message;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setSuccess(true);
        result.setMessage("");
        result.setData(data);
        return result;
    }

    public static Result fail(String errMsg, Object data) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(errMsg);
        result.setData(data);
        return result;
    }

    public static Result fail(String errMsg) {
        Result result = new Result();
        result.setSuccess(false);
        result.setMessage(errMsg);
        result.setData(null);
        return result;
    }

}
