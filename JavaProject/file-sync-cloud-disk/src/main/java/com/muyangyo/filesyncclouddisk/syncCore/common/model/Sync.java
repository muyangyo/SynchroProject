package com.muyangyo.filesyncclouddisk.syncCore.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/20
 * Time: 12:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sync {
    private String localPath;//本地路径
    private boolean isFirstSync;//是否第一次同步
    private String username;//用户名
    private String password;//密码
}
