package com.muyangyo.project_management_system.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/25
 * Time: 12:45
 */
@Component
@Slf4j
public class TokenUtil {
    //token有效期
    private static final long LIFETIME = 30 * 60 * 1000;

    private static String SIGNATURE;

    private static SecretKey KEY = null;

    /**
     * 根据 yml 文件中的签名,进行初始化
     */
    @Value("${MD5.signature}")
    private void init(String signature) {
        if (!StringUtils.hasLength(signature)) {
            throw new NullPointerException("请配置JWT的签名!");
        }
        SIGNATURE = signature;
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SIGNATURE));
    }

    /**
     * 检验Token是否有效
     */
    public static boolean checkToken(String token) {
        JwtParser build = Jwts.parserBuilder().setSigningKey(KEY).build();
        Claims body = null;
        try {
            body = build.parseClaimsJws(token).getBody();
        } catch (Exception e) {
//            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 返回Token的载荷部分,如果token为null时,返回空Map
     *
     * @param token token字符串
     * @return Map形式的载荷
     */
    public static Map<String, String> getTokenLoad(String token) {
        JwtParser build = Jwts.parserBuilder().setSigningKey(KEY).build();
        Claims body = null;
        try {
            body = build.parseClaimsJws(token).getBody();
            //将载荷部分转为Map
            Map<String, String> map = new HashMap<>();
            Set<Map.Entry<String, Object>> entries = body.entrySet();
            for (Map.Entry<String, Object> tmp : entries) {
                map.put(tmp.getKey(), tmp.getValue().toString());
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("转义Token时出现错误,已返回一个空的Map!");
            return new HashMap<>(0);
        }
    }

    /**
     * 将 map 中的内容设置到 Token 里面,返回 Token 字符串
     *
     * @param map 需要设置的参数
     * @return Token字符串
     */
    public static String getToken(Map<String, String> map) {
        String token = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + LIFETIME))
                .signWith(KEY)
                .compact();
        return token;
    }
}
