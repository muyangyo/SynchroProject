package com.muyangyo.wechatsmallprogram.global.mapper;

import com.muyangyo.wechatsmallprogram.global.model.AccessToken;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface AccessTokenMapper {
    void insertToken(AccessToken accessToken);
    List<AccessToken> findByUserId(int userId);
    void updateToken(AccessToken accessToken);
    void deleteToken(int id);
}
