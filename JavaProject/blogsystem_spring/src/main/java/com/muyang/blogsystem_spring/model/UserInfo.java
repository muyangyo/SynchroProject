package com.muyang.blogsystem_spring.model;

import lombok.Data;

// 对user的信息进去截取部分
@Data
public class UserInfo {
    private int id;
    private String userName;
    private String githubUrl;
    private int articlesCount;
    private boolean author = false;
}