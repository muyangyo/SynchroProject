package com.demo.springdemo231023.IoC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/6
 * Time: 9:14
 */
@Component
public class Test {

    @Qualifier("ex1")
    @Autowired
    public Extension extension;
}
