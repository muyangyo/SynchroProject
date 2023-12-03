package com.example.aop.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 22:42
 */
@Slf4j

@Aspect
@Order(1)
@Component
public class TimeAspect {
    @Pointcut("execution(* com.example.aop.web.*.*(..))")
    public void PT() {
    }


    @Before("com.example.aop.aop.TimeAspect.PT()")
    public void doBefore() {
        log.info("执行方法前");
    }

    @SneakyThrows
    @Around("com.example.aop.aop.TimeAspect.PT()")
    public Object base(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        log.info("Around-before");

        //执行目标方法
        Object ret = joinPoint.proceed();

        log.info("Around-after");
        long end = System.currentTimeMillis();
        log.info(joinPoint.getSignature() + "方法执行的时间为: {} 毫秒", end - start);
        return ret;
    }


    @After("@annotation(com.example.aop.annotation.MyAnnotation)")
    public void doAfter() {
        log.info("执行方法后");
    }


}


//    @Around("execution(* com.example.aop.web.*.*(..))")
//    @Before("execution(* com.example.aop.web.*.*(..))")
//    @After("execution(* com.example.aop.web.*.*(..))")
