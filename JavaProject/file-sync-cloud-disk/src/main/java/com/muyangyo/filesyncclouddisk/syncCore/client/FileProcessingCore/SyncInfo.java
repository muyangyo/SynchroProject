package com.muyangyo.filesyncclouddisk.syncCore.client.FileProcessingCore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/19
 * Time: 22:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncInfo {
    private String localPath;
    private boolean isFirst;
    private String username;
    private String password;
}
