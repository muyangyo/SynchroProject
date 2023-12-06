package com.example.aop.proxy;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLibIntercepter implements MethodInterceptor {
    //被代理的对象
    private Object target;

    public CGLibIntercepter(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("CGLib开始代理...");
        //调用目标对象
        Object result = methodProxy.invoke(target, objects);
        System.out.println("CGLib结束代理...");
        return result;
    }
}
