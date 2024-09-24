package com.muyangyo.wechatsmallprogram.global.temp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class RedirectUtils {

    // 创建 RestTemplate 实例，用于发送 HTTP 请求
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 处理请求的重定向
     *
     * @param request  当前的 HTTP 请求
     * @param routeUrl 目标重定向 URL
     * @param prefix   URL 前缀
     * @return ResponseEntity 重定向后的响应
     */
    public ResponseEntity<String> redirect(HttpServletRequest request, String routeUrl, String prefix) {
        RequestEntity<?> requestEntity = null;
        try {
            // 创建重定向 URL
            String redirectUrl = createRedirectUrl(request, routeUrl, prefix);
            // 创建请求实体
            requestEntity = createRequestEntity(request, redirectUrl);
        } catch (Exception e) {
            // 记录错误信息
            log.error("映射失败!", e);
        }
        // 执行请求并返回响应
        return route(requestEntity);
    }

    /**
     * 创建重定向 URL
     *
     * @param request  当前的 HTTP 请求
     * @param routeUrl 目标重定向 URL
     * @param prefix   URL 前缀
     * @return 构建好的重定向 URL
     */
    private String createRedirectUrl(HttpServletRequest request, String routeUrl, String prefix) {
        // 获取查询字符串
        String queryString = request.getQueryString();
        // 构建重定向 URL
        return routeUrl + request.getRequestURI().replace(prefix, "") +
                (queryString != null ? "?" + queryString : "");
    }

    /**
     * 创建请求实体
     *
     * @param request 当前的 HTTP 请求
     * @param url     重定向的目标 URL
     * @return 请求实体
     * @throws URISyntaxException URI 语法错误
     * @throws IOException        IO 异常
     */
    private RequestEntity<byte[]> createRequestEntity(HttpServletRequest request, String url)
            throws URISyntaxException, IOException {
        // 获取请求方法
        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        // 解析请求头
        MultiValueMap<String, String> headers = parseRequestHeader(request);
        // 解析请求体
        byte[] body = parseRequestBody(request);
        // 返回构建好的请求实体
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    /**
     * 执行 HTTP 请求
     *
     * @param requestEntity 请求实体
     * @return 响应实体
     */
    private ResponseEntity<String> route(RequestEntity<?> requestEntity) {
        // 使用 RestTemplate 执行请求并返回响应
        return restTemplate.exchange(requestEntity, String.class);
    }

    /**
     * 解析请求体
     *
     * @param request 当前的 HTTP 请求
     * @return 请求体的字节数组
     * @throws IOException IO 异常
     */
    private byte[] parseRequestBody(HttpServletRequest request) throws IOException {
        try (InputStream inputStream = request.getInputStream()) {
            return StreamUtils.copyToByteArray(inputStream); // 将输入流转换为字节数组
        }
    }

    /**
     * 解析请求头
     *
     * @param request 当前的 HTTP 请求
     * @return 请求头的 MultiValueMap
     */
    private MultiValueMap<String, String> parseRequestHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        // 获取请求头名称列表
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            // 获取当前请求头的所有值
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headerValue : headerValues) {
                // 将头信息添加到 HttpHeaders 对象中
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }
}
