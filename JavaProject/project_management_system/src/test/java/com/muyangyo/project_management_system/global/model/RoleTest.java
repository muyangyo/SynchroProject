package com.muyangyo.project_management_system.global.model;

import org.junit.jupiter.api.Test;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 9:52
 */
class RoleTest {
    @Test
    void getCode() {
        Role role = Role.ROOT;
        System.out.println(role.toString());
        System.out.println(role.getCode());
    }

    @Test
    void setCode() {
        Role role = Role.ROOT;
        System.out.println(role.getCode());
//        role.setCode(1);
        System.out.println(role.getCode());
    }
}