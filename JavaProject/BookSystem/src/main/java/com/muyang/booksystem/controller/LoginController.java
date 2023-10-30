package com.muyang.booksystem.controller;

import com.muyang.booksystem.dao.User;
import com.muyang.booksystem.service.LoginServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    private final LoginServer loginServer = new LoginServer();

    @RequestMapping("/Login")
    public boolean Login(String userName, String password, HttpSession session) {
        User user = new User(userName, password);
        System.out.println(user);
        if (loginServer.login(user)) {
            session.setAttribute("userName", user.getName());
            return true;
        }
        return false;
    }
}
