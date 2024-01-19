package com.muyang.blogsystem_spring.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/19
 * Time: 12:43
 */
@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void getUserById() {
        System.out.println(userMapper.getUserById(1));
    }

    @Test
    void getUserByName() {
        System.out.println(userMapper.getUserByName("admin1"));
        System.out.println(userMapper.getUserByName("admin"));
    }

    @Test
    void getAllUsers() {
        System.out.println(userMapper.getAllUsers().toString());
    }
}