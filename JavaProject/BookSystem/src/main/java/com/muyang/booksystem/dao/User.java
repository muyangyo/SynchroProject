package com.muyang.booksystem.dao;


import lombok.Data;
import lombok.NonNull;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:05
 */
@Data
public class User {
    @NonNull
    private String name;
    @NonNull
    private String password;

    private String status;//用户状态
}
