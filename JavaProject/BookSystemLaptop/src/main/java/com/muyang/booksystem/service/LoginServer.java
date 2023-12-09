package com.muyang.booksystem.service;

import com.muyang.booksystem.mapper.UserMapper;
import com.muyang.booksystem.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:12
 */
@Service
public class LoginServer {
    @Autowired
    UserMapper userMapper;

    public boolean loginCheck(User user) {
        if (user == null || !StringUtils.hasLength(user.getUserName()) || !StringUtils.hasLength(user.getPassword())) {
            return false;
        }

        User temp = userMapper.selectOne(user);
        if (temp == null) {
            return false;
        }

        if (temp.getUserName().equals(user.getUserName()) && temp.getPassword().equals(user.getPassword())) {
            return true;
        }
        return false;
    }
}

