package com.example.recoder.starter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.UUID;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 9:55
 */
@Configuration
@Data
@Slf4j
public class GetStartPort {
    @Value("${server.port}")
    private Integer port;

    private String token;

    @Override
    public String toString() {
        return port.toString();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void start() {
        log.info("网页服务成功启动于: http://127.0.0.1:" + port);
        token = UUID.randomUUID().toString();
        log.info("Token为:" + token);
    }
}
