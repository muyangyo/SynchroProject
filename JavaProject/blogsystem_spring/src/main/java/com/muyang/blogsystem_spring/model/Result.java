package com.muyang.blogsystem_spring.model;

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
    private StatusCode statusCode;
    private String errMsg;
    private Object data;

    public static Result success(Object data) {
        Result result = new Result();
        result.setStatusCode(StatusCode.SUCCESS);
        result.setErrMsg("");
        result.setData(data);
        return result;
    }

    public static Result fail(String errMsg, Object data) {
        Result result = new Result();
        result.setStatusCode(StatusCode.ERROR);
        result.setErrMsg(errMsg);
        result.setData(data);
        return result;
    }

    public static Result fail(String errMsg) {
        Result result = new Result();
        result.setStatusCode(StatusCode.ERROR);
        result.setErrMsg(errMsg);
        result.setData(null);
        return result;
    }

}
