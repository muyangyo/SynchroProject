package com.muyangyo.wechatsmallprogram.global.model;

import lombok.Data;

import java.util.Date;

@Data
public class AccessToken {
    private int id;
    private int userId;
    private String token;
    private Date setTime;
}
