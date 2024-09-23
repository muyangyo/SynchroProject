import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/23
 * Time: 20:10
 */
public class Main {
    private static final String signature = "RVU01PjU6a0fr8AVtwEHc0wUCVbGjo9hTlYzRiI3Zf4=";
    private static final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(signature));
//                                     分钟   秒   毫秒
    private static final long lifeSpan = 30 * 60 * 1000;


//    //获取token中载荷
//    public static Claims getMapFromToken(String token) {
//        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
//        Claims body = null;
//        try {
//            body = build.parseClaimsJws(token).getBody();
//        } catch (Exception e) {
////            log.warn("解析失败!", e);
//            return null;
//        }
//        return body;
//    }
//
    //验证token
    public static boolean checkToken(String token) {
        JwtParser build = Jwts.parserBuilder().setSigningKey(key).build();
        Claims body = null;
        try {
            body = build.parseClaimsJws(token).getBody();
        } catch (Exception e) {
//            log.warn("解析失败! 请检查 Token 是否存在或者过期");
            return false;
        }
        return true;
    }

    //生成token
    public static String getToken(Map<String, String> map) {
        String token = Jwts.builder().setClaims(map) //设置载荷
                .setExpiration(new Date(System.currentTimeMillis() + lifeSpan)).signWith(key).compact();
        return token;
    }


//    //生成 256位 基字符串(手动调用,基字符串是不能更改的)
//    private static void genBaseString() {
//        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String baseString = Encoders.BASE64.encode(secretKey.getEncoded());
//        System.out.println(baseString);
//    }
//
//    public static void main(String[] args) {
//        genBaseString();
//    }

//    public static void main(String[] args) {
////        genBaseString();
//        Map<String, String> map = new HashMap<>();
//        map.put("userid", "123");
//        String token = getToken(map);
//        System.out.println(token);
//        token = "eyJhbGciOiJIUzM4NCJ9.eyJ1c2VyaWQiOiIxMjMiLCJleHAiOjE3MDU3Mjk1Mjl9.VpoGy11dpEuVNpcC0TX42nyELNFSzL9QG5Haw1We8cliO_0aLBbNs3qCTbL0rbQf";
//        System.out.println(checkToken(token));
//    }


}
