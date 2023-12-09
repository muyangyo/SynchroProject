package com.muyang.booksystem.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/26
 * Time: 10:59
 */
//@Component
public class WebManager implements WebMvcConfigurer {
    @Autowired
    private CheckLoginInterceptor checkLoginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(checkLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/**/*.html", "/**/*.css", "/**/*.js", "/**/*.png","/LoginController/Login");
    }
}
