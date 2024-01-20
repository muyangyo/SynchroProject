package com.muyang.blogsystem_spring.aop;

import com.muyang.blogsystem_spring.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 10:06
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class ErrorHandler {
    @ExceptionHandler
    public Result Exception(Exception e) {
        log.error("发生异常: ", e);
        return Result.fail("服务器内部异常!请联系管理员!");
    }

    @ExceptionHandler
    public Result nullException(NullPointerException e) {
        log.error("空指针异常: ", e);
        return Result.fail("查无此数据,请按照页面标准发送请求");
    }
}
