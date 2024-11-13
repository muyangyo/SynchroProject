package com.example.curdbuilder.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Users)实体类
 *
 * @author 沐阳Yo
 * @since 2024-11-13 16:16:09
 */
public class Users implements Serializable {
    private static final long serialVersionUID = 559176764739232260L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String username;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 上次登入时间
     */
    private Date lastLoginTime;
    /**
     * 0: 禁用 1:启用
     */
    private Integer status;
    /**
     * 空间使用大小
     */
    private Long useSpace;
    /**
     * 总空间大小
     */
    private Long totalSpace;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUseSpace() {
        return useSpace;
    }

    public void setUseSpace(Long useSpace) {
        this.useSpace = useSpace;
    }

    public Long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(Long totalSpace) {
        this.totalSpace = totalSpace;
    }

}

