package com.example.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK动态代理的实现
 */
public class JDKInvocation implements InvocationHandler {
    //目标对象
    private Object target;

    public JDKInvocation(Object target) {
        this.target = target;
    }

    /**
     * 参数说明
     * proxy：代理对象
     * method：代理对象需要实现的方法，即其中需要重写的方法
     * args：method所对应方法的参数
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始代理...");
        //调用目标对象
        //proxy是代理对象
        Object result = method.invoke(target, args);//底层通过反射来实现的
        System.out.println("结束代理...");
        return result;
    }
}
