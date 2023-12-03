package com.example.aop.web;

import com.example.aop.annotation.MyAnnotation;
import com.example.aop.model.Demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 21:07
 */
@RestController
public class Web {
    @MyAnnotation
    @RequestMapping(value = "/f1", produces = "application/json")
    public String f1() {
        return "信息";
    }

    @MyAnnotation
    @RequestMapping("/f2")
    public Demo f2() {
        Demo demo = new Demo("demo", 1);
        return demo;
    }

    @MyAnnotation
    @RequestMapping("/f3")
    public Demo f3() {
        Demo demo = null;
        demo.setInteger(1);
        return demo;
    }

    @MyAnnotation
    @RequestMapping("/f4")
    public void f4() {
    }


}
