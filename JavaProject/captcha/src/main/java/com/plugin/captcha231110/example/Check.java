package com.plugin.captcha231110.example;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/13
 * Time: 15:06
 */
@RequestMapping("/checkCaptcha")
@RestController
public class Check {
    @RequestMapping("/notTime")
    public Boolean checkNotTime(String captcha, HttpSession session) {
        if (!StringUtils.hasLength(captcha)) return false;
        String key = session.getAttribute("ADMIN_KAPTCHA_SESSION_KEY").toString();
        return key.equals(captcha);
    }
}
