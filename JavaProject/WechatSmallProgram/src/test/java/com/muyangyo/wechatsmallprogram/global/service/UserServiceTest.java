package com.muyangyo.wechatsmallprogram.global.service;

import com.muyangyo.wechatsmallprogram.global.Component.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/24
 * Time: 21:27
 */
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void loginService() {
        Result result = userService.loginService("123");
        System.out.println(result);

        Result result1 = userService.loginService("1234");
        System.out.println(result1);
    }
}