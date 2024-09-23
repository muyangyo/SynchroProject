package com.muyangyo.project_management_system.global.model;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 9:38
 */
public enum Role {

    ROOT(0), USER(1), TOURIST(2);
    private final int code;

    Role(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Role getRoleFromInt(int i) {
        Role[] roles = {ROOT, USER, TOURIST};
        return roles[i];
    }

}
