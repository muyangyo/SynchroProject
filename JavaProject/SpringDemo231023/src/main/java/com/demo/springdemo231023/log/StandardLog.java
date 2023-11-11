package com.demo.springdemo231023.log;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/10
 * Time: 17:42
 */
@Component
public class StandardLog {
    public Logger logger = LoggerFactory.getLogger(StandardLog.class);

    //    public Logger logger = LoggerFactory.getLogger("StandardLog.class");
    /*@PostConstruct
    public void print() {
        System.out.println();//美观用

        logger.error("error 级日志");
        logger.warn("warn 级日志");
        logger.info("info 级日志");
        logger.debug("debug 级日志");
        logger.trace("trace 级日志");

        System.out.println();//美观用
    }*/
}
