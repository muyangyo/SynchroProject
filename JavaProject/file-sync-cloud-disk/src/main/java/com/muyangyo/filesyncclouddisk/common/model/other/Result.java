package com.muyangyo.filesyncclouddisk.common.model.other;

import com.muyangyo.filesyncclouddisk.common.model.enums.StatusCode;
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

    /**
     * 成功的返回结果
     *
     * @param data 返回数据
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setStatusCode(StatusCode.SUCCESS);
        result.setErrMsg("");
        result.setData(data);
        return result;
    }

    /**
     * 失败的返回结果
     *
     * @param errMsg 错误信息
     * @param data   返回数据
     */
    public static Result fail(String errMsg, Object data) {
        Result result = new Result();
        result.setStatusCode(StatusCode.FAILURE);
        result.setErrMsg(errMsg);
        result.setData(data);
        return result;
    }

    /**
     * 失败的返回结果
     *
     * @param errMsg 错误信息
     */
    public static Result fail(String errMsg) {
        Result result = new Result();
        result.setStatusCode(StatusCode.FAILURE);
        result.setErrMsg(errMsg);
        result.setData("");
        return result;
    }

    /**
     * 发生错误时的返回结果
     *
     * @param errMsg 错误信息
     * @param data   返回数据
     * @return
     */
    public static Result error(String errMsg, Object data) {
        Result result = new Result();
        result.setStatusCode(StatusCode.ERROR);
        result.setErrMsg(errMsg);
        result.setData(data);
        return result;
    }

    /**
     * 发生错误时的返回结果
     *
     * @param errMsg 错误信息
     */
    public static Result error(String errMsg) {
        Result result = new Result();
        result.setStatusCode(StatusCode.ERROR);
        result.setErrMsg(errMsg);
        result.setData("");
        return result;
    }

}
