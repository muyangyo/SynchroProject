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
 * Date: 2024/12/18
 * Time: 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationLogVO {
    @NoNeedMetaMapping
    private String operationTime;
    private String operation;
    private String userName;
    private String userIp;
    private OperationLevel operationLevel;
}
