package com.muyangyo.project_management_system.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/25
 * Time: 13:08
 */
@SpringBootTest
@Slf4j
class TokenUtilTest {

    @Test
    void checkToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VybmFtZSI6IjEyMyIsImV4cCI6MTcwODg0MTIxNH0.NsGivbxVTcBsS-mh_k6cU23DUh9lV6PJtGl4ztD99kXOg0GeR2JwaX_FYZg9JfK5GRflvkvddpdBm8mSX4o_3A";
        boolean b = TokenUtil.checkToken(token);
        if (b) {
            log.info("token解析成功!");
        } else {
            log.warn("token解析失败!");
        }
    }

    @Test
    void getToken() {
        Map<String, String> map = new HashMap<>(1);
        map.put("username", "123");
        String token = TokenUtil.getToken(map);
        log.info(token);
    }

    @Test
    void getTokenLoad() {
        Map<String, String> map = new HashMap<>(1);
        map.put("username", "123");
        map.put("gen", "男");
        String token = TokenUtil.getToken(map);
        log.info("已生成Token: " + token);
        Map<String, String> tokenLoad = TokenUtil.getTokenLoad(token);
        log.info("该Token的载荷部分为以下信息:\n");
        for (Map.Entry<String, String> tmp : tokenLoad.entrySet()) {
            System.out.println(tmp.getKey() + " : " + tmp.getValue());
        }
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
}