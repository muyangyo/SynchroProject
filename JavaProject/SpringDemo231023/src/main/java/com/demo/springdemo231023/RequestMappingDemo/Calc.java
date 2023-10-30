package com.demo.springdemo231023.RequestMappingDemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/29
 * Time: 22:47
 */
@RequestMapping("/calc")
@RestController
public class Calc {
    @RequestMapping("/sum")
    public String sum(Integer num1, Integer num2) {

        return (num1 + num2)+"";
    }
}
