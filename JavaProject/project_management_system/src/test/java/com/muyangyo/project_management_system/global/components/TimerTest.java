package com.muyangyo.project_management_system.global.components;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 10:35
 */
@SpringBootTest
class TimerTest {
    @Autowired
    Timer timer;

    @Test
    void getFormatTime() {
        System.out.println(timer.getFormatTime());
    }

    @Test
    void getYear() {
        System.out.println(timer.getYear());
    }

    @Test
    void getMonth() {
        System.out.println(timer.getMonth());
    }

    @Test
    void getDayOfMonth() {
        System.out.println(timer.getDayOfMonth());
    }

    @Test
    void getDayOfWeek() {
        System.out.println(timer.getDayOfWeek());
    }
}