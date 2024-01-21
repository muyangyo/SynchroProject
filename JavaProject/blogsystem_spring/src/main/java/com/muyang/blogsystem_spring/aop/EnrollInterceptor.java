package com.muyang.blogsystem_spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 14:31
 */
@Component
public class EnrollInterceptor implements WebMvcConfigurer {
    @Autowired
    Interceptor interceptor;

    List<String> excludeURL = Arrays.asList("/**/*.html", "/**/*.css", "/**/*.css.map",
            "/**/*.js", "/**/*.png", "/fonts/**", "/favicon.ico", "/editor.md/**",
            "/user/login", "/user/register"
    );

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(excludeURL);
    }
}
