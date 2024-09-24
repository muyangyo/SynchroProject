package com.muyangyo.wechatsmallprogram.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyangyo.wechatsmallprogram.global.Component.Result;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 8:32
 */
@RestControllerAdvice
public class FormatReturn implements ResponseBodyAdvice {
    @Autowired
    ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof String) {
//            return Result.success(body); 不能这样写,会类型转化出错!
            return objectMapper.writeValueAsString(Result.success(body));//如果方法放回的数据是String类型则必须使用这种方式转化一下
        } else if (body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
