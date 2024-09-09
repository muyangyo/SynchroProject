package com.muyangyo.project_management_system.global.model.response_model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 9:26
 */
@Data
@AllArgsConstructor
public class LoadAfterLogin {
    public String userName;
    public String role;
    public String lastLogin;
}
