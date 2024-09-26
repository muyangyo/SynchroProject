package com.muyangyo.wechatsmallprogram.global.temp;

import com.muyangyo.wechatsmallprogram.global.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE openid = #{openid}")
    User findByOpenId(String openid);

    @Insert("INSERT INTO users (openid, session_key, latest_login_time) VALUES (#{openid}, #{sessionKey}, #{latestLoginTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE users SET session_key = #{sessionKey}, latest_login_time = #{latestLoginTime} WHERE id = #{id}")
    void update(User user);
}
