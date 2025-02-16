package com.muyangyo.filesyncclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId; // 用户id
    private String username; // 用户名
    private String password; // 密码
    private Date createTime; // 创建时间
    private Date lastLoginTime; // 最后登录时间
    private Integer accountStatus; // 账户状态 0-注销 1-正常
    /**
     * 1. 空的话: 没有权限
     * 2. r:读权限
     * 3. w:写权限
     * 4. d:删除权限
     */
    private String permissions; // 权限

    public Boolean haveReadPermission() {
        return permissions.contains("r");
    }

    public Boolean haveWritePermission() {
        return permissions.contains("w");
    }

    public Boolean haveDeletePermission() {
        return permissions.contains("d");
    }

    public Set<Character> getPermissionsSet() {
        Set<Character> permissionSet = new HashSet<>();
        for (Character c : permissions.toCharArray()) {
            permissionSet.add(c);
        }
        return permissionSet;
    }
}