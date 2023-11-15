package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/15
 * Time: 23:02
 */
@SpringBootTest
@Slf4j
class UserXMLMapperTest {

    @Autowired
    UserXMLMapper userXMLMapper;

    @Test
    void insert1() {
        User user = new User();
        user.setUsername("小李");
        user.setPassword("123");
        user.setAge(18);
        userXMLMapper.insert1(user);
    }

    @Test
    void insert2() {
        User user = new User();
        user.setUsername("小李");
        user.setPassword("123");
        user.setAge(18);
        log.info("user的ID为:{}", user.getId()); // 为null
        userXMLMapper.insert2(user);
        log.info("user的ID为:{}", user.getId()); // 为新插入的值
    }

    @Test
    void insert3() {
        User user = new User();
        user.setUsername("小李");
        user.setPassword("123");
        user.setAge(18);
        userXMLMapper.insert3(user);
    }

    @Test
    void delete() {
        User user = new User();
        user.setId(16);
        userXMLMapper.delete(user);
    }

    @Test
    void update() {
        User user = new User();
        user.setId(1);
        user.setAge(100);
        userXMLMapper.update(user);
    }

    @Test
    void select1() {
        List<User> list = userXMLMapper.select1();
        log.info(list.toString());
    }

    @Test
    void select2() {
        List<User> list = userXMLMapper.select2();
        log.info(list.toString());
    }

    @Test
    void select3() {
        List<User> list = userXMLMapper.select3();
        log.info(list.toString());
    }
}