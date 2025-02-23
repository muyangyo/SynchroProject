package com.muyangyo.filesyncclouddisk.common.model.vo;

import converter.MetaMapping;
import converter.NoNeedMetaMapping;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 21:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncInfoOfServerVO {
    @MetaMapping("username")
    private String syncName; // 同步名称
    @MetaMapping("localAddress")
    private String localPath; // 本地地址
    @NoNeedMetaMapping
    private long localSize; // 本地大小
    @NoNeedMetaMapping
    private boolean versionDelete; // 是否删除版本
}
