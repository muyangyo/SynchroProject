package com.muyang.blogsystem_spring.model;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/19
 * Time: 12:31
 */
@Data
public class Blog {
    private int id;
    private String title;//标题
    private String content;//正文
    private int userId;//作者id
    private int deleteFlag;
    private Date createTime;
    private Date updateTime;

    public String getCreateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dateFormat.format(createTime);
    }

    @Override
    public String toString() {
        return "Blog(" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", deleteFlag=" + deleteFlag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ')';
    }
}
