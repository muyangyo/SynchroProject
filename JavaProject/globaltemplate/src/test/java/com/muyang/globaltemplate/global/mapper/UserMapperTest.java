package com.muyang.globaltemplate.global.mapper;

import com.muyang.globaltemplate.global.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 17:32
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void getByUsername() {
        User user = new User();
        user.setUsername("admin");
//        user.setUsername("123");
        User ret = userMapper.getByUsername(user);
        System.out.println(ret);
    }
}