package com.muyangyo.fileclouddisk.common.utils;

import com.muyangyo.fileclouddisk.common.config.Setting;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/2/25
 * Time: 12:45
 */
@Slf4j
public class TokenUtils {
    private static SecretKey KEY = null;// 签名
    private static long LIFETIME = 1000 * 60 * 60; // 1小时

    public static void init(Setting setting) {
        KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(setting.getSignature()));
        LIFETIME = setting.getLifeTime() * 1000;
    }

    /**
     * 验证Token是否有效,并验证IP是否一致
     *
     * @param token token字符串
     * @param IP    IP地址
     * @return true:有效,false:无效
     */
    public static boolean checkToken(String token, String IP) {
        return parseToken(token).map(claims -> claims.get("IP", String.class)).map(ip -> ip.equals(IP)).orElse(false);
    }

    /**
     * 返回Token的载荷部分,如果token为null时,返回空Map
     *
     * @param token token字符串
     * @return Map形式的载荷
     */
    public static Map<String, String> getTokenLoad(String token) {
        return parseToken(token).map(claims -> {
            Map<String, String> map = new HashMap<>();
            claims.forEach((key, value) -> map.put(key, value.toString()));
            return map;
        }).orElseGet(() -> {
            log.warn("转义Token时出现错误,已返回一个空的Map!");
            return new HashMap<>(0);
        });
    }

    /**
     * 将 map 中的内容设置到 Token 里面,返回 Token 字符串
     *
     * @param map 需要设置的参数
     * @return Token字符串
     */
    public static String getToken(Map<String, String> map) {
        return Jwts.builder().setClaims(map).setExpiration(new Date(System.currentTimeMillis() + LIFETIME)).signWith(KEY).compact();
    }

    /**
     * 解析Token并返回Claims
     *
     * @param token token字符串
     * @return Optional<Claims>
     */
    private static Optional<Claims> parseToken(String token) {
        try {
            JwtParser parser = Jwts.parserBuilder().setSigningKey(KEY).build();
            Claims body = parser.parseClaimsJws(token).getBody();
            return Optional.of(body);
        } catch (Exception e) {
            log.warn("解析Token时出现错误: {}", e.getMessage());
            return Optional.empty();
        }
    }
}