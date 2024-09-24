package com.muyangyo.wechatsmallprogram.global.service;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/24
 * Time: 15:41
 */


import com.muyangyo.wechatsmallprogram.global.Component.Result;
import com.muyangyo.wechatsmallprogram.global.mapper.UserMapper;
import com.muyangyo.wechatsmallprogram.global.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public Result loginService(String openId) {
        // 处理登录逻辑
        if (!StringUtils.hasLength(openId)) {
            return Result.fail("openId为空");
        }
        User user = userMapper.findByOpenId(new Integer(openId));
        if (user == null) {
            return Result.fail("没有查到对应的用户!");
        }


        log.info(user + " 用户登入中...");


        return Result.success(user);
    }

    //
//    public Result saveProfile(Map<String, Object> data) {
//        // 处理保存用户信息逻辑
//    }
//
//    public Result checkSession(Map<String, String> data) {
//        // 处理检查会话逻辑
//    }
//}
}

