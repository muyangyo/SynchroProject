package com.example.chfs.config;

// 导入必需的类

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;

// 标记该类为Spring组件
@Component
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler {

    // 定义常量，用于存储非静态文件的属性名称
    public final static String ATTR_FILE = "NON-STATIC-FILE";

    // 重写方法以获取资源
    @Override
    protected Resource getResource(HttpServletRequest request) {
        // 从请求中获取文件路径
        String filePath = (String) request.getAttribute(ATTR_FILE);
        // 返回所请求的文件作为文件系统资源
        return new FileSystemResource(filePath);
    }
}
