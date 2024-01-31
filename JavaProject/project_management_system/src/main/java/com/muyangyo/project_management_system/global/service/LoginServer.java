package com.muyangyo.project_management_system.global.service;

import com.muyangyo.project_management_system.global.mapper.UserMapper;
import com.muyangyo.project_management_system.global.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:24
 */
@Service
@Slf4j
public class LoginServer {
    @Autowired
    UserMapper userMapper;

    public boolean check(User user) {
        User ret = userMapper.getByUsername(user);

        // 避免空指针问题
        if (ret == null) {
            log.info("没有该用户名数据");
            return false;
        }


        log.info("查询到了该用户{},验证中...", ret);
        return (ret.getUsername().equals(user.getUsername())) && ret.getPassword().equals(user.getPassword());
    }
}
