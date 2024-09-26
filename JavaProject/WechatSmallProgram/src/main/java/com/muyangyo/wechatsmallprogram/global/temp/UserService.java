package com.muyangyo.wechatsmallprogram.global.temp;

import com.muyangyo.wechatsmallprogram.global.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper; // MyBatis Mapper for User table

    public User findOrCreateUser(String openid, String sessionKey) {
        User user = userMapper.findByOpenId(openid);
        if (user == null) {
            user = new User();
            user.setOpenId(openid);
            user.setSessionKey(sessionKey);
            user.setLatestLoginTime(new Date());
            userMapper.insert(user);
        } else {
            user.setSessionKey(sessionKey);
            user.setLatestLoginTime(new Date());
            userMapper.update(user);
        }
        return user;
    }

    public void saveAccessToken(Integer userId, String token) {
        // Save token logic
    }
}
