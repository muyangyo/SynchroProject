package com.muyang.booksystem.controller;

import com.muyang.booksystem.model.User;
import com.muyang.booksystem.service.LoginServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:39
 */
@RequestMapping("/LoginController")
@RestController
public class LoginController {
    @Autowired
    private LoginServer loginServer;


    @RequestMapping("/Login")
    public boolean Login(String userName, String password, HttpSession session) {
        User user = new User(userName, password);
        System.out.println(user);
        if (loginServer.loginCheck(user)) {
            session.setAttribute("userName", user.getUserName());
            return true;
        }
        return false;
    }
}
