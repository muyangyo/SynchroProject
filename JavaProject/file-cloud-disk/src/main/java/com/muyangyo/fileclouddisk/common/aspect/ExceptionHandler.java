package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.fileclouddisk.common.exception.IllegalPath;
import com.muyangyo.fileclouddisk.common.exception.OperationWithoutPermission;
import com.muyangyo.fileclouddisk.common.model.other.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 8:54
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(Exception e) {
        log.error("发生未指定捕获异常!");
        log.error("异常信息:", e);
        return Result.error("服务器发生未知异常!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(UncategorizedSQLException e) {
        log.error("数据库操作异常!", e);
        return Result.error("服务器发生未知异常!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(IllegalLoginWithoutRSA e) {
        log.error("非正常登入!", e);
        return Result.error("请使用客户端登入!如果您使用的是页面,请刷新后再试!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(IllegalPath e) {
        log.error("路径非法!", e);
        return Result.error("请使用客户端操作!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(OperationWithoutPermission e) {
        log.error("操作权限不足!");
        return Result.error("您没有操作权限!");
    }
}
