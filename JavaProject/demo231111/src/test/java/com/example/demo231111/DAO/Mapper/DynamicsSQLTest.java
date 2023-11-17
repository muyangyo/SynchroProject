package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/17
 * Time: 22:10
 */
@SpringBootTest
@Slf4j
class DynamicsSQLTest {

    @Autowired
    DynamicsSQL dynamicsSQL;

    @Test
    void insert() {
        User user = new User();
        user.setUsername("小神");
        user.setPassword("root");
        user.setAge(18);
        user.setGender(1);
        dynamicsSQL.insert(user);
    }

    @Test
    void delete() {
        dynamicsSQL.delete(Arrays.asList(6, 7, 8, 9, 10, 11, 13, 14, 15, 17, 18, 19, 20));
    }

    @Test
    void select() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("admin");
        user.setAge(100);
//        user.setGender(0);
        dynamicsSQL.select(user);
    }

    @Test
    void update() {
        User user = new User();
        user.setId(3);
        user.setUsername("小白");
        user.setPassword("root");
        user.setAge(18);
//        user.setGender(0);
        dynamicsSQL.update(user);
    }
}