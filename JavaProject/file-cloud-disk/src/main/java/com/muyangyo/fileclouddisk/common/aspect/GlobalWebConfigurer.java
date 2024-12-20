package com.muyangyo.fileclouddisk.common.aspect;

import com.muyangyo.fileclouddisk.common.config.Setting;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
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
public class GlobalWebConfigurer implements WebMvcConfigurer {
    @Resource
    VerifyLoginInterceptor verifyLoginInterceptor;
    @Resource
    Setting setting;

    private static final List<String> EXTERNALLY_ACCESSIBLE_PATHS = new LinkedList<>(
            Arrays.asList("/api/file/getShareFile", "/api/file/OutsideFileDownload"));

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyLoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXTERNALLY_ACCESSIBLE_PATHS)
                .excludePathPatterns(STATIC_RESOURCE_PATHS); // 放行静态资源;
    }

    private static final List<String> STATIC_RESOURCE_PATHS = new LinkedList<>(
            Arrays.asList("/*.html", "/*.ico", "/assets/**", "/"));
}