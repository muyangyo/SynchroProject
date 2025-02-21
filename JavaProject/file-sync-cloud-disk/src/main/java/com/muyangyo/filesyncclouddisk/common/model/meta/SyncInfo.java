package com.muyangyo.filesyncclouddisk.common.model.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/20
 * Time: 20:54
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncInfo {
    private String username; // FTPS用户名(主键)
    private String password; // FTPS密码
    private String localAddress; // 本地地址 (唯一)
    // 上面是都有,下面是客户端才有的
    private boolean isFirst; // 是否是第一次同步,默认是
    private String serverId; // 服务器ID(MAC)
    private String serverIp; // 服务器IP
    private boolean isActive; // 是否开启同步,默认是
    private Date lastSyncTime; // 上次同步的时间
}
