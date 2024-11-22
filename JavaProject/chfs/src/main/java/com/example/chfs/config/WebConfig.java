package com.example.chfs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig 类用于配置Spring MVC的相关设置，主要是添加跨域访问支持。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 添加CORS映射，允许来自特定源的请求。
     *
     * @param registry CORS注册对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 允许所有路径的请求
                .allowedOrigins("http://localhost:5173")  // 仅允许来自指定源的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的HTTP方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);  // 允许发送凭证（如cookies）
//                .maxAge(3600);  // 预检请求的有效期
    }
}
