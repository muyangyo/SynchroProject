package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/11
 * Time: 18:38
 */
@Slf4j
@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    void returnAll() {
        List<User> list = userMapper.returnAll(1);
        System.out.println(list);
    }

    @BeforeEach
    void setUp() {
        log.info("正在测试一个方法");
    }

    @AfterEach
    void tearDown() {
        log.info("一个方法测试完成,准备测试另一个方法");
    }

    @Test
    void returnAll1() {
        List<User> list = userMapper.returnAll1(1);
        System.out.println(list);
    }

    @Test
    void returnAll2() {
        List<User> list = userMapper.returnAll2(1);
        System.out.println(list);
    }

    @Test
    void insert1() {
        User user = new User();
        user.setUsername("小白");
        user.setPassword("123");
        user.setAge(18);
        userMapper.insert1(user);
    }

    @Test
    void insert2() {
        User user = new User();
        user.setUsername("小白");
        user.setPassword("123");
        user.setAge(18);
        log.info("user的ID为:{}", user.getId()); // 为null
        userMapper.insert2(user);
        log.info("user的ID为:{}", user.getId()); // 为新插入的值
    }

    @Test
    void insert3() {
        User user = new User();
        user.setUsername("小白");
        user.setPassword("123");
        user.setAge(18);
        userMapper.insert3(user);
    }
}