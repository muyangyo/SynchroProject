package com.muyang.blogsystem_spring.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 9:34
 */
@Data
public class Comment {
    private int id;
    private String userName;//评论者
    private String content;//评论内容
    private int blogId;//评论文章id
    private int deleteFlag;
    private Date createTime;
    private Date updateTime;

    public String getCreateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(createTime);
    }
}
