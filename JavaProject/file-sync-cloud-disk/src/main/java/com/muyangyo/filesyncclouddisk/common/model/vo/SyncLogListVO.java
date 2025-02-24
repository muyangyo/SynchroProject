package com.muyangyo.filesyncclouddisk.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/24
 * Time: 17:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncLogListVO {
    private int pageSize;
    private int total;
    private List<SyncLogVO> list;
}
