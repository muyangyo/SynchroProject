package com.example.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 23:55
 */
@Retention(RetentionPolicy.RUNTIME) //存活时间
@Target({ElementType.METHOD}) //作用目标
public @interface MyAnnotation {
}
