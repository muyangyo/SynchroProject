package com.muyangyo.project_management_system.global.model;

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
    private String username;
    private String password;
    private String nickname;
    private int gender;// 0为女  1为男  2为秘密
    private Role role;
    private Date createTime;
    private Date updateTime;
}
