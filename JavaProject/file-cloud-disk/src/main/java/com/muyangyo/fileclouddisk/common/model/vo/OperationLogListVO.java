package com.muyangyo.fileclouddisk.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/18
 * Time: 18:48
 */
@Data
@AllArgsConstructor
public class OperationLogListVO {
    private int pageSize;
    private int total;
    private List<OperationLogVO> list;
}
