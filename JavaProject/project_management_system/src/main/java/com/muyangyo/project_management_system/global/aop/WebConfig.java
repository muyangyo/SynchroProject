package com.muyangyo.project_management_system.global.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/31
 * Time: 21:48
 */
@Component
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    VerifyLogin verifyLogin;

    List<String> EXCLUDE_URL = new LinkedList<>(Arrays.asList("/**/*.css", "/**/*.css.map",
            "/**/*.js", "/**/*.png", "/fonts/**", "/favicon.ico"
    ));

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:login.html");
        registry.addViewController("").setViewName("forward:login.html");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        EXCLUDE_URL.add("/");
        EXCLUDE_URL.add("/login.html");
        EXCLUDE_URL.add("/global/login");
        EXCLUDE_URL.add("/error");
        registry.addInterceptor(verifyLogin).addPathPatterns("/**").excludePathPatterns(EXCLUDE_URL);
    }
}
