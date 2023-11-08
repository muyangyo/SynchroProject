package com.demo.springdemo231023.Configs;

import lombok.Data;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/8
 * Time: 17:08
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "dtype")
public class DType {
    private Map<String, String> map;
}
