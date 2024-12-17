package com.muyangyo.fileclouddisk.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/17
 * Time: 19:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListVO {
    private String username;
    private String password;
    private List<String> permissions;
}
