package com.example.recoder.constant;

import lombok.Data;

@Data
public class User {
    private Integer id; // 自增 ID
    private String openId; // 用户唯一标识符
    private String avatarUrl; // 用户头像 URL
    private String nickName; // 用户昵称(自己定义的)
}

