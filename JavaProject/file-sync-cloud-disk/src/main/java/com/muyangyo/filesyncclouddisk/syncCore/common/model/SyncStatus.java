package com.muyangyo.filesyncclouddisk.syncCore.common.model;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/19
 * Time: 16:00
 */
public enum SyncStatus {
    WAIT_CONNECT,// 等待连接
    CONNECTING,// 连接中
    SYNCING,// 同步中
    SYNC_COMPLETE,// 同步完成
    SYNC_FAILED,// 同步失败
    DISCONNECT,// 断开连接
}
