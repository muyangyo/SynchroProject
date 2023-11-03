package com.demo.springdemo231023.IOC;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/3
 * Time: 19:32
 */

@Configuration
public class Extension {
    public void run() {
        System.out.println("Configuration-Extension: hello");
    }
}
