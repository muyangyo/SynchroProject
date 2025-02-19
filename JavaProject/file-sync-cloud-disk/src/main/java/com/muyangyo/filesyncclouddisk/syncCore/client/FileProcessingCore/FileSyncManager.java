package com.muyangyo.filesyncclouddisk.syncCore.client.FileProcessingCore;

import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import com.muyangyo.filesyncclouddisk.syncCore.server.ServerMain;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/19
 * Time: 22:50
 */
public class FileSyncManager {
    private static volatile SyncStatus SYNC_STATUS = SyncStatus.WAIT_CONNECT; // 同步状态(默认等待连接)

    /**
     * 设置同步状态
     *
     * @param status 状态枚举
     * @return true
     */
    public synchronized static boolean setSyncStatus(SyncStatus status) {
        SYNC_STATUS = status;
        return true;
    }

    /**
     * 获取同步状态
     *
     * @return 同步状态枚举
     */
    public synchronized static SyncStatus getSyncStatus() {
        return SYNC_STATUS;
    }

    FileSyncManager(String serverIp, int serverPort) {
        // 假设这里是从数据库中查出来了
    }

    public static void main(String[] args) {
        FileSync manager = new FileSync("C:/Users/MuYang/Desktop/local"
                , true, "localhost", ServerMain.FTPS_SERVER_PORT,
                ServerMain.FTPS_USER_NAME, ServerMain.FTPS_SERVER_OA_PASSWORD);
        manager.syncFiles();
    }
}
