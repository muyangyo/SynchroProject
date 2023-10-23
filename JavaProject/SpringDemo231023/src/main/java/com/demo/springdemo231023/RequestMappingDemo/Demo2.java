package com.demo.springdemo231023.RequestMappingDemo;

import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/23
 * Time: 17:55
 */
@RequestMapping("/demo2")
@RestController
public class Demo2 {

    @RequestMapping("/m1")
    public String m1(int id) {
        return "接收到了参数:" + id;
    }

    @RequestMapping("/m2")
    public String m2(String name, Integer id) {
        return "接收到了 name 参数:" + name + " id 参数:" + id;
    }

    @RequestMapping("/m3")
    public String m3(Person person) {
        return "接收到了参数:" + person;
    }

    @RequestMapping("/m4")
    public String m4(@RequestParam("name") String username) {
        return "接收到了参数:" + username;
    }

    @RequestMapping("/m5")
    public String m5(@RequestParam(value = "name", required = false) String username) {
        return "接收到了参数:" + username;
    }
}
