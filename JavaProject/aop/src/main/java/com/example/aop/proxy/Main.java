package com.example.aop.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        HouseSubject target = new RealHouseSubject();


        //静态代理
        System.out.println("静态代理:");
        HouseProxy proxy1 = new HouseProxy(target);
        proxy1.rentHouse();
        System.out.println("====================================");

        //JDK动态代理
        //目标对象1  代理接口(需要代理的动作)
        System.out.println("JDK动态代理:");
        HouseSubject proxy2 = (HouseSubject) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class[]{HouseSubject.class},
                new JDKInvocation(target));
        proxy2.rentHouse();
        System.out.println("====================================");


        //目标对象2: 代理类
        RealHouseSubjectClass subjectClass = new RealHouseSubjectClass();
        System.out.println("CGLib动态代理接口:");
        //CGLib 动态代理
        HouseSubject proxy3 = (HouseSubject) Enhancer.create(target.getClass(), new CGLibIntercepter(target));
        proxy3.rentHouse();
        System.out.println("====================================");

        //CGLib 代理类
        System.out.println("CGLib动态代理类:");
        RealHouseSubjectClass proxy4 = (RealHouseSubjectClass) Enhancer.create(subjectClass.getClass(), new CGLibIntercepter(subjectClass));
        proxy4.rentHouse();
        System.out.println("====================================");


    }
}
