package com.muyang.blogsystem_spring.service;

import com.muyang.blogsystem_spring.mapper.BlogMapper;
import com.muyang.blogsystem_spring.mapper.UserMapper;
import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.model.User;
import com.muyang.blogsystem_spring.model.UserInfo;
import com.muyang.blogsystem_spring.tools.TokenTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 11:26
 */


@Service
@Slf4j
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    BlogMapper blogMapper;

    public UserInfo getUserInfo(String token) throws Exception {
        Claims map = TokenTool.getMapFromToken(token);
        if (map == null) {
            throw new Exception("无效token");
        }
        int userId = Integer.parseInt((String) map.get("userId"));
        User user = userMapper.selectUserById(userId);
        UserInfo userInfo = new UserInfo();
        if (user == null) {
            return null;
        }
        userInfo.setId(user.getId());
        userInfo.setUserName(user.getUserName());
        userInfo.setGithubUrl(user.getGithubUrl());
        userInfo.setArticlesCount(blogMapper.selectBlogCount(userId));
        log.info("已获取用户信息: " + userInfo);
        return userInfo;
    }

    public Result checkLogin(String userName, String password) {
        User user = userMapper.selectUserByName(userName);
        if (user == null) {
            return Result.fail("账号或者密码错误!");
        }
        if (userName.equals(user.getUserName()) && password.equals(user.getPassword())) {

            Map<String, String> map = new HashMap<>(2);
            map.put("userId", String.valueOf(user.getId()));
            map.put("LastLogin", String.valueOf(System.currentTimeMillis()));

            String token = TokenTool.getToken(map);
            log.info(user + " 成功登录");
            return Result.success(token);
        }
        return Result.fail("账号或者密码错误!");
    }

    public Result register(User user) {
        //先查询下有没有同名账号
        User temp = userMapper.selectUserByName(user.getUserName());
        if (temp != null) {
            return Result.fail("有同名账号,请取别的名字!");
        }
        int i = userMapper.insertUsers(user);
        if (i > 0) {
            log.info(user + " 成功注册");
            return Result.success(null);
        }
        return Result.fail("注册出现错误!请联系管理员");
    }

    public UserInfo getEditorUserInfo(String token, int blogId) throws Exception {
        Claims map = TokenTool.getMapFromToken(token);
        if (map == null) {
            throw new Exception("无效token");
        }
        int userId = Integer.parseInt((String) map.get("userId"));//当前账户的ID
        Blog curBlog = blogMapper.selectBlogById(blogId);//当前博客的ID
        if (curBlog == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        //验证是否是作者本人
        if (curBlog.getUserId() == userId) {
            //是作者本人
            userInfo.setAuthor(true);
        }
        //不是本人的话,只要获取其他信息即可,不需要设置作者为true
        User user = userMapper.selectUserById(curBlog.getUserId());
        userInfo.setId(user.getId());
        userInfo.setUserName(user.getUserName());
        userInfo.setGithubUrl(user.getGithubUrl());
        userInfo.setArticlesCount(blogMapper.selectBlogCount(curBlog.getUserId()));
        log.info("获取博主信息 " + userInfo);
        return userInfo;
    }
}
