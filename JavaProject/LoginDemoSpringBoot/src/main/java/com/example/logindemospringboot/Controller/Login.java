package com.example.logindemospringboot.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/10/18
 * Time: 21:29
 */
@RestController
public class Login {
    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/login")
    public String login(String username, String password, HttpServletRequest httpServletRequest) {
        if (username.equals("root") && password.equals("123")) {
            HttpSession session = httpServletRequest.getSession(true);
            session.setAttribute("username", username);
            session.setAttribute("password", password);
            return "登入成功!";
        }
        return "登入失败";
    }

    @RequestMapping("/getUserInfo")
    public String login(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            return null;
        }
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");

        return objectMapper.writeValueAsString(new User(username, password));
    }


}
