package com.muyangyo.filesyncclouddisk.common.aspect;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/26
 * Time: 19:51
 * Desc: 为了支持range请求，需要自定义一个Handler，用于处理视频文件的二进制流。
 */
@Component
public class FileBinHandler extends ResourceHttpRequestHandler {

    public final static String ATTR_FILE = "NON-STATIC-FILE";    // 定义常量，用于存储非静态文件的属性名称


    // 重写方法,用于获取资源
    @Override
    protected Resource getResource(HttpServletRequest request) {
        // 从请求中获取文件路径
        String filePath = (String) request.getAttribute(ATTR_FILE);
        // 返回所请求的文件作为文件系统资源
        return new FileSystemResource(filePath);
    }
}
