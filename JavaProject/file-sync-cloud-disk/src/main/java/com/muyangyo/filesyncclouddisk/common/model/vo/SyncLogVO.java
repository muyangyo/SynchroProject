package com.muyangyo.filesyncclouddisk.common.model.vo;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import converter.NoNeedMetaMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/24
 * Time: 17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncLogVO {
    @NoNeedMetaMapping
    private String operationTime; // 操作时间
    private String operation;           // 操作
    private String targetFile;          // 操作文件
    private String operator;            // 操作者
    private String ip;                  // 操作者IP
    private OperationLevel level;       // 操作等级
}
