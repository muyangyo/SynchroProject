package com.muyangyo.project_management_system.global.model.request_model;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 18:00
 */
@Data
public class ForLogin {
    private String username;
    private String password;
    private Boolean rememberMe;
}
