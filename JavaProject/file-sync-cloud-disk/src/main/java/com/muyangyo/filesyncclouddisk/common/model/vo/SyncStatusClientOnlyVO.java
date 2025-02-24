package com.muyangyo.filesyncclouddisk.common.model.vo;

import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/24
 * Time: 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncStatusClientOnlyVO {
    private String syncName;
    private SyncStatus syncStatus;
}
