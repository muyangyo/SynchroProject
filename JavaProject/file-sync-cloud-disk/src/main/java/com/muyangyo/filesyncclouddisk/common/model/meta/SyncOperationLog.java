package com.muyangyo.filesyncclouddisk.common.model.meta;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 16:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncOperationLog {
    private Long id; // 主键
    private Date operationTime; // 操作时间
    private String operation;           // 操作类型
    private String targetFile;          // 操作文件
    private String operator;            // 操作者
    private String ip;                  // 操作者IP
    private OperationLevel level;       // 操作等级
}
