package com.demo.springdemo231023.IoC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/4
 * Time: 21:17
 */
@Component
public class Extension {
    String inf;

    public Extension(String inf) {
        this.inf = inf;
    }

    @Autowired
    public Extension() {
    }

    public void run() {
        System.out.println(inf + "-Extension");
    }
}
