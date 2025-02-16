package com.muyangyo.filesyncclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private String userId; // 用户id
    private String username;// 用户名
    private String password; // 密码
    private Date createTime; // 创建时间
    private Date lastLoginTime; // 最后登录时间
}