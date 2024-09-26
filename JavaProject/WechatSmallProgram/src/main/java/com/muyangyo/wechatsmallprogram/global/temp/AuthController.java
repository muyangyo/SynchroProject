package com.muyangyo.wechatsmallprogram.global.temp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyangyo.wechatsmallprogram.global.model.User;
import com.muyangyo.wechatsmallprogram.global.temp.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Value("${WeChatSettings.appId}")
    private String appId;

    @Value("${WeChatSettings.appSecret}")
    private String appSecret;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) throws JsonProcessingException {
        String code = loginData.get("code");
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing code");
        }

        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        Map<String, Object> responseBody = new ObjectMapper().readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
        });

        if (responseBody.containsKey("openid") && responseBody.containsKey("session_key")) {
            String openid = (String) responseBody.get("openid");
            String sessionKey = (String) responseBody.get("session_key");

            User user = userService.findOrCreateUser(openid, sessionKey);
            String token = UUID.randomUUID().toString();

            // Store token to DB and send it back to client
            userService.saveAccessToken(user.getId(), token);

            return ResponseEntity.ok(Map.of("success", true, "userInfo", user, "accessToken", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed, invalid user info");
        }
    }
}
