package com.muyangyo.filesyncclouddisk.common.model.dto;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 22:46
 */
@Data
public class CreateSyncDTO {
    String syncName;
    String localPath;
    String key;
}
