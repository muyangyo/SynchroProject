package com.muyang.booksystem.model;


import lombok.Data;
import lombok.NonNull;

import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:05
 */
@Data
public class User {
    private int id;
    private String userName;
    private String password;
    private String deleteFlag;//为 0 时,是删除状态
    private Date createTime;
    private Date updateTime;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
