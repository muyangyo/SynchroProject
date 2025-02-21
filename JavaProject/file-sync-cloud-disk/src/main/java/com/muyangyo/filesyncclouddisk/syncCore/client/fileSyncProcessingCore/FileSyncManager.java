package com.muyangyo.filesyncclouddisk.syncCore.client.fileSyncProcessingCore;

import com.muyangyo.filesyncclouddisk.common.model.meta.SyncInfo;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncInfoMapper;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncLogMapper;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/19
 * Time: 22:50
 */
@Slf4j
public class FileSyncManager {

    public ConcurrentHashMap<String, FileSyncOneShotProcessor> CURRENT_SYNCS = new ConcurrentHashMap<>(); // 同步列表, key为本地路径, value为 FileSyncOneShotProcessor 对象

    public FileSyncManager(String serverIp, int serverPort, List<SyncInfo> syncs, ExecutorService settingThreadPool, SyncInfoMapper syncInfoMapper, SyncLogMapper syncLogMapper) {


        for (SyncInfo sync : syncs) {

            if (sync.isActive()) {

                settingThreadPool.submit(() -> {

                    try {
                        log.info("开始执行本地路径为 [{}] 的同步", sync.getLocalAddress());
                        long startTime = System.currentTimeMillis();
                        FileSyncOneShotProcessor fileSyncOneShotProcessor = new FileSyncOneShotProcessor(sync.getLocalAddress(),
                                sync.isFirst(), serverIp, serverPort, sync.getUsername(), sync.getPassword(),syncInfoMapper, syncLogMapper);
                        CURRENT_SYNCS.put(sync.getLocalAddress(), fileSyncOneShotProcessor); // 加入同步列表
                        fileSyncOneShotProcessor.syncFiles();// 执行同步
                        log.info("执行本地路径为 [{}] 的同步成功,耗时为 [{}] 秒", sync.getLocalAddress(), (System.currentTimeMillis() - startTime) / 1000.0);
                    } catch (IOException e) {
                        log.error("执行本地路径为 [{}] 的同步失败", sync.getLocalAddress(), e);
                        CURRENT_SYNCS.get(sync.getLocalAddress()).setSyncStatus(SyncStatus.SYNC_FAILED); // 设置同步状态为失败,这个状态会多线程访问的
                    }

                });

            } else {
                log.warn("本地路径为 [{}] 的同步未开启, 跳过同步", sync.getLocalAddress());
            }

        }
    }
}
