package com.muyangyo.fileclouddisk.common.model.dto;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/30
 * Time: 14:59
 */
@Data
public class RenameFileDTO {
    private String oldPath;
    private String newName;
}