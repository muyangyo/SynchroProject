package com.muyangyo.filesyncclouddisk.common.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/31
 * Time: 21:48
 */
@Component
public class GlobalInterceptor implements WebMvcConfigurer {
    @Autowired
    VerifyLogin verifyLogin;

//    List<String> EXCLUDE_URL = new LinkedList<>(Arrays.asList("/user/login", "/user/register")); //注意: 不需要加 context-path


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyLogin).addPathPatterns("/**"); //.excludePathPatterns(EXCLUDE_URL);
    }
}
