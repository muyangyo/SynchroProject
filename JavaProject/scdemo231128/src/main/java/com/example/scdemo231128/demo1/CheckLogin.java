package com.example.scdemo231128.demo1;

import com.example.scdemo231128.mapper.UserMapper;
import com.example.scdemo231128.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/28
 * Time: 23:07
 */
@RequestMapping("/login")
@Slf4j
@RestController
public class CheckLogin {
    @Autowired
    UserMapper userMapper;

    @RequestMapping("/check")
    public String RetJSON(User user) {
        User ret = userMapper.selectUserById(user.getUsername());
        log.info(ret.toString());
        if (ret.getUsername().equals(user.getUsername()) && ret.getPassword().equals(user.getPassword()))
            return "成功登录! 账户信息为:" + ret.toString();
        return "账户或者密码错误!";
    }
}
