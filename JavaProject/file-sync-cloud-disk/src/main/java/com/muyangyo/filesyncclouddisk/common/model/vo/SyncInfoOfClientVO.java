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
 * Time: 21:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncInfoOfClientVO {
    @MetaMapping("username")
    private String syncName; // 同步名称
    @MetaMapping("localAddress")
    private String localPath; // 本地地址
    @NoNeedMetaMapping
    private long localSize; // 本地文件大小
    private String serverIp; // 服务器IP
    private boolean isActive; // 是否开启同步,默认是
    @NoNeedMetaMapping
    private String lastSyncTime; // 上次同步的时间
}
