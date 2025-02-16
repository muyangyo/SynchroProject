package com.muyangyo.filesyncclouddisk.common.aspect;

import com.muyangyo.filesyncclouddisk.common.exception.IllegalLoginWithoutRSA;
import com.muyangyo.filesyncclouddisk.common.exception.IllegalPath;
import com.muyangyo.filesyncclouddisk.common.exception.OperationWithoutPermission;
import com.muyangyo.filesyncclouddisk.common.model.other.Result;
import com.muyangyo.filesyncclouddisk.common.utils.NetworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

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
    public Result exception(Exception e, HttpServletRequest request) {
        log.error("来自IP [{}] 的请求触发了未捕获的异常！URL: [{}]", NetworkUtils.getClientIp(request), request.getRequestURL());
        log.error("异常信息:", e);
        return Result.error("服务器发生未知异常!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.warn("请求方法不支持: 来自IP [{}] 的请求使用了不支持的HTTP方法！URL: [{}], 错误信息: {}", NetworkUtils.getClientIp(request), request.getRequestURL(), e.getMessage());
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
