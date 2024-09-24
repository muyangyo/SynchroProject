package com.muyangyo.wechatsmallprogram.global.mapper;

import com.muyangyo.wechatsmallprogram.global.model.UserLevel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLevelMapper {
    void insertUserLevel(UserLevel userLevel);
    UserLevel findByUserId(int userId);
    void updateUserLevel(UserLevel userLevel);
    void deleteUserLevel(int id);
}
