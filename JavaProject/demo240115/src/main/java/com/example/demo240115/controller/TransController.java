package com.example.demo240115.controller;

import com.example.demo240115.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequestMapping("/trans")
@RestController
public class TransController {
    @Autowired
    private UserService userService;

    /**
     * 正常操作, 没有抛出异常
     */

    @Transactional
    @RequestMapping("/registry")
    public String registry(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        return "注册成功";
    }

    /**
     * 抛出异常, 事务回滚
     * 程序报错, 没有处理
     */
    @Transactional
    @RequestMapping("/r2")
    public String registry2(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        int a = 10 / 0;
        return "注册成功";
    }

    /**
     * 程序报错, 异常捕获
     * 事务提交
     */
    @Transactional
    @RequestMapping("/r3")
    public String registry3(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错");
        }
        return "注册成功";
    }

    /**
     * 程序报错, 重新抛出异常
     * 事务回滚
     */
    @Transactional
    @RequestMapping("/r4")
    public String registry4(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错");
            throw e;
        }
        return "注册成功";
    }

    /**
     * 程序报错, 手动让事务回滚
     * 事务回滚
     */

    @Transactional
    @RequestMapping("/r5")
    public String registry5(String userName, String password) {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        try {
            int a = 10 / 0;
        } catch (ArithmeticException e) {
            log.info("程序出错");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  //当前事务回滚
        }
        return "注册成功";
    }

    /**
     * 程序报错, 重新抛出异常
     * 事务回滚
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    @RequestMapping("/r6")
    public String registry6(String userName, String password) throws IOException {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错");
            throw new IOException("程序出现IO异常");
        }
        return "注册成功";
    }

    /**
     * 程序报错, 重新抛出异常
     * 事务回滚
     */

    @Transactional(rollbackFor = {Exception.class, Error.class}) //这个是一个数组,当这些 异常类或其子类 发生时,会回滚
    @RequestMapping("/r7")
    public String registry7(String userName, String password) throws IOException {
        Integer result = userService.insertUser(userName, password);
        log.info("用户插入成功, result:" + result);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info("程序出错" + e.toString());
//            throw new IOException();
        }
        return "注册成功";
    }
}
