package com.muyangyo.filesyncclouddisk.common.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/4
 * Time: 17:29
 */
@Data
public class ShareFileListVO {
    private int total; // 总数
    private int pageSize; // 每页显示数量
    private List<ShareFileVO> list; // 分页数据
}
