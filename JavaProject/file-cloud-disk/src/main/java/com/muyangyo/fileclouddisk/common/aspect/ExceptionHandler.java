package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.exception.IllegalLoginWithoutRSA;
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
        e.printStackTrace();
        return Result.error("服务器发生未知异常!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(UncategorizedSQLException e) {
        log.error("数据库操作异常!");
        e.printStackTrace();
        return Result.error("服务器发生未知异常!");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public Result exception(IllegalLoginWithoutRSA e) {
        log.warn("非正常登入!");
        e.printStackTrace();
        return Result.error("请使用客户端的登入!");
    }


}
