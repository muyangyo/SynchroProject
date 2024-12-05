package com.muyangyo.fileclouddisk.common.model.dto;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/5
 * Time: 13:13
 */

@Data
public class CreateFolderDTO {
    private String folderName;
    private String parentPath;
}
