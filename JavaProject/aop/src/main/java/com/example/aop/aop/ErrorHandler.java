package com.example.aop.aop;

import com.example.aop.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 22:01
 */
@Slf4j
@ResponseBody

@ControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    public Result NullException(NullPointerException e) {
        log.error("发生空指针异常:", e);
        return Result.fail(null, "服务器内部错误!");
    }

    @ExceptionHandler
    public Result Exception(Exception e) {
        log.error("发生异常:", e);
        return Result.fail(null, "服务器内部错误!");
    }

    @ExceptionHandler
    public Result Exception(ArithmeticException e) {
        log.error("发生算术异常:", e);
        return Result.fail(null, "服务器内部错误!");
    }
}
