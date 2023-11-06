package com.demo.springdemo231023.IoC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/4
 * Time: 21:18
 */
@Component
public class Demo {
    @Bean
    public Extension ex1() {
        return new Extension("ex1");
    }

    @Bean
    public Extension ex2() {
        return new Extension("ex2");
    }
}
