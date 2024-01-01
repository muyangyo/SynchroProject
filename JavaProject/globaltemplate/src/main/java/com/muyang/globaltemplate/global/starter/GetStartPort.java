package com.muyang.globaltemplate.global.starter;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 9:55
 */
@Configuration
@Data
public class GetStartPort {
    @Value("${server.port}")
    private Integer port;

    @Override
    public String toString() {
        return port.toString();
    }
}
