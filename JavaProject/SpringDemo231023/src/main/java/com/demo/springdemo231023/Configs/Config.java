package com.demo.springdemo231023.Configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/8
 * Time: 16:16
 */
@Configuration //必须有五大注释
public class Config {


//    @Value("${Demo.key2}")
//    private String string;

    // @PostConstruct

    /* @Autowired
     private Student sd;*/
//    @Value("${demo.key1}")
//    private String key;

    @Autowired
    private DType dType;

    public void outConfig() {
        System.out.println(dType);
    }
}
