package com.example.demo240115.controller;

import com.example.demo240115.service.LogService;
import com.example.demo240115.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示事务的传播机制
 */
@Slf4j
//@RequestMapping("/proga")
@RestController
public class ProController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;

    /**
     * 正常操作, 没有抛出异常
     */

    @Transactional
    @RequestMapping("/p1")
    public String p1(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);

        Integer result2 = logService.insertLog(userName, "用户自行注册");
        log.info("日志表插入成功, result:" + result2);
        return "注册成功";
    }
}
