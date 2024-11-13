package com.muyangyo.filesyncclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;         // 用户ID
    private String username;       // 昵称
    private String password;       // 密码
    private Date createTime;       // 创建时间
    private Date lastLoginTime;    // 上次登录时间
    private Integer status;        // 状态：0 禁用，1 启用
    private Long useSpace;         // 已使用空间大小
    private Long totalSpace;       // 总空间大小
}