package com.muyangyo.wechatsmallprogram.global.mapper;

import com.muyangyo.wechatsmallprogram.global.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findById(int id);

    User findByOpenId(int openId);

    List<User> findAll();

    void updateUser(User user);

    void deleteUser(int id);
}
