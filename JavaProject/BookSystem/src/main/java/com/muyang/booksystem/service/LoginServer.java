package com.muyang.booksystem.service;

import com.muyang.booksystem.dao.User;
import org.springframework.util.StringUtils;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:12
 */
public class LoginServer {
    public boolean login(User user) {
        if (user == null || !StringUtils.hasLength(user.getName()) || !StringUtils.hasLength(user.getPassword())) {
            return false;
        }
        //假用户
        User root = new User("root", "root");

        if (root.getName().equals(user.getName()) && root.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}

