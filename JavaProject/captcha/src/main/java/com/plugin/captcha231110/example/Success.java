package com.plugin.captcha231110.example;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/13
 * Time: 15:06
 */
@RestController
public class Success {
    @RequestMapping("success")
    public String success() {
        return "登录成功!";
    }
}
