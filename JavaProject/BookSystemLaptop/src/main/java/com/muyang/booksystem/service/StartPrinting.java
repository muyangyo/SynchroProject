package com.muyang.booksystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/2
 * Time: 18:53
 */
@Component
@Slf4j
public class StartPrinting {
    @Value("${server.port}")
    private int port;

    public void defaultPrint() {
        log.info("\n启动网址为: http://127.0.0.1:" + port);
    }
}
