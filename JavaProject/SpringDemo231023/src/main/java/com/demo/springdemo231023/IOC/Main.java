package com.demo.springdemo231023.IOC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/3
 * Time: 19:33
 */
public class Main {
    @Autowired
    Extension extension;

    public void run() {
        extension.run();
    }
}
