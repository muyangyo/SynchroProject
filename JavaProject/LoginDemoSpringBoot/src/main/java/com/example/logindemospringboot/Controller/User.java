package com.example.logindemospringboot.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/10/18
 * Time: 21:41
 */
@Data
@AllArgsConstructor
public class User {
    private String username;
    private String password;
}
