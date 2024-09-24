package com.muyangyo.wechatsmallprogram.global.model;

import lombok.Data;

import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 11:24
 */
@Data
public class User {
    private int id;
    private String openid;
    private String sessionKey;
    private String nickname;
    private String avatar;
    private Date createTime;
    private Date latestLoginTime;
    private int status;
}
