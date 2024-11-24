package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.config.Setting;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/31
 * Time: 21:48
 */
@Component
public class GlobalInterceptor implements WebMvcConfigurer {
    @Resource
    VerifyLogin verifyLogin;
    @Resource
    Setting setting;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyLogin).addPathPatterns("/**");
    }
}
