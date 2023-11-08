package com.demo.springdemo231023.Configs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/8
 * Time: 16:52
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "student")
public class Student {
    private Integer id;
    private String name;
    private Integer age;
}
