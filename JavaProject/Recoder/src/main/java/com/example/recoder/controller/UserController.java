package com.example.recoder.controller;

import com.example.recoder.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/26
 * Time: 9:59
 */
@RestController
@RequestMapping("/user")
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

    @RequestMapping("/addUser")
    public void addUser() {

    }
}
