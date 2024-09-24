package com.muyangyo.wechatsmallprogram.global.controller;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/24
 * Time: 15:41
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyangyo.wechatsmallprogram.global.Component.Result;
import com.muyangyo.wechatsmallprogram.global.model.httpModel.LoginRequest;
import com.muyangyo.wechatsmallprogram.global.model.httpModel.WXResponse;
import com.muyangyo.wechatsmallprogram.global.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RestTemplate restTemplate;//RestTemplate是Spring框架中用于同步客户端HTTP访问的模板工具类
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${WeChatSettings.appId}")
    private String appId;
    @Value("${WeChatSettings.appSecret}")
    private String appSecret;

    /**
     * 登入接口
     */
    @RequestMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest) throws IOException {
        if (!StringUtils.hasLength(loginRequest.getCode())) {
            return Result.fail("登入失败,没有找到 Code!");
        }
        //发送请求获取授权信息
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + loginRequest.getCode() + "&grant_type=authorization_code";
        String str = restTemplate.getForObject(url, String.class);//请求地址,返回类型
//        WXResponse wxResponse = restTemplate.getForObject(url, WXResponse.class);//请求地址,返回类型(无法正常映射)
        WXResponse wxResponseBody = objectMapper.readValue(str, WXResponse.class);

        if (wxResponseBody.getErrCode() != null) {
            log.error("微信授权失败: {} ,请求网站为: '{}' ", wxResponseBody, url);
            return Result.fail("微信授权失败");
        }
        log.info("成功获取微信授权信息: {} ", wxResponseBody);
        userService.loginService(wxResponseBody.getOpenId());

        return null;
    }


//    @PostMapping("/save_profile")
//    public ResponseEntity<?> saveProfile(@RequestBody Map<String, Object> data) {
//        return userService.saveProfile(data);
//    }
//
//    @PostMapping("/check_session")
//    public ResponseEntity<?> checkSession(@RequestBody Map<String, String> data) {
//        return userService.checkSession(data);
//    }
//


    /**
     * 打印完整请求
     */
    @RequestMapping("/rq")
    public void rd(HttpServletRequest httpServletRequest) { // 打印请求方法
        System.out.println("Request Method: " + httpServletRequest.getMethod());

        // 打印请求URL
        System.out.println("Request URL: " + httpServletRequest.getRequestURL());

        // 打印请求头
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = httpServletRequest.getHeader(headerName);
            System.out.println("Header: " + headerName + " = " + headerValue);
        }

        // 打印请求体
        StringBuilder requestBody = new StringBuilder();
        try (BufferedReader reader = httpServletRequest.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Request Body: " + requestBody.toString());
    }
}

