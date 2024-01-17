package com.muyang.globaltemplate.global.controller;

import com.muyang.globaltemplate.global.model.User;
import com.muyang.globaltemplate.global.requestmodel.ForLogin;
import com.muyang.globaltemplate.global.service.LoginServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:24
 */
@RestController
@RequestMapping("/global")
@Slf4j
public class LoginController {

    @Autowired
    LoginServer loginServer;

    @RequestMapping( value = "/login",method = RequestMethod.POST)
    public Boolean login(@RequestBody(required = false) ForLogin forLogin) {
        /*类型转换*/
        User user = new User();
        user.setUsername(forLogin.getUsername());
        user.setPassword(forLogin.getPassword());
        log.info(user+"登录中...");
        return loginServer.check(user);
    }
}
