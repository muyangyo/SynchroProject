package com.demo.springdemo231023.IoC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
    @Autowired
    public Extension ex;

    /*@Autowired
    public Demo(Extension ex)
        this.extension = extension;
    }*/


    /*public Demo() {
    }*/
}
