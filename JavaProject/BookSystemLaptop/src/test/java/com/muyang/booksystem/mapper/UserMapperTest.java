package com.muyang.booksystem.mapper;

import com.muyang.booksystem.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/20
 * Time: 17:24
 */
@SpringBootTest
class UserMapperTest {

    @Autowired
    UserMapper userMapper;

    @Test
    void selectAll() {
        System.out.println(userMapper.selectAll().toString());
    }

    @Test
    void selectOne() {
        User user = new User();
        user.setId(1);
//        user.setUserName("admin");
        System.out.println(userMapper.selectOne(user).toString());

    }
}