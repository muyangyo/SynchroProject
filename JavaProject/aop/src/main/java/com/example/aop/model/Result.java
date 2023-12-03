package com.example.aop.model;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 21:22
 */
@Data
public class Result {
    private ResultCode resultCode;
    private String errMsg;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setErrMsg("");
        result.setData(data);
        return result;
    }

    public static Result fail(Object data, String err) {
        Result result = new Result();
        result.setResultCode(ResultCode.ERROR);
        result.setErrMsg(err);
        result.setData(data);
        return result;

    }
}
