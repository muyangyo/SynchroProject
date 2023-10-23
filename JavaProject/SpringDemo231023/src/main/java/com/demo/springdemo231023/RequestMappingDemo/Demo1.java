package com.demo.springdemo231023.RequestMappingDemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/23
 * Time: 17:41
 */
@RequestMapping("/Demo1")
@RestController
public class Demo1 {

    @RequestMapping("/hello")
    public String sayHello1() {
        return "hello,my first SpringMVC";
    }

    @RequestMapping(value = "/helloOnlyGet", method = RequestMethod.GET)
    public String sayHello2() {
        return "hello(only get)";

    }
}
