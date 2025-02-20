package com.muyangyo.filesyncclouddisk.syncCore.client.FileProcessingCore;

import com.muyangyo.filesyncclouddisk.syncCore.common.model.Sync;
import com.muyangyo.filesyncclouddisk.syncCore.common.model.SyncStatus;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/19
 * Time: 22:50
 */
@Slf4j
//@Component
public class FileSyncManager {

    public ConcurrentHashMap<String, FileSyncOneShotProcessor> CURRENT_SYNCS = new ConcurrentHashMap<>(); // 同步列表, key为本地路径, value为 FileSyncOneShotProcessor 对象

    public FileSyncManager(String serverIp, int serverPort, List<Sync> syncs) {
//        ExecutorService executor = Executors.newCachedThreadPool(); //todo: 后面这里从Setting里取线程

        for (Sync sync : syncs) {

//            executor.submit(() -> {

                try {
                    log.info("开始执行本地路径为 [{}] 的同步", sync.getLocalPath());
                    long startTime = System.currentTimeMillis();
                    FileSyncOneShotProcessor fileSyncOneShotProcessor = new FileSyncOneShotProcessor(sync.getLocalPath(), sync.isFirstSync(), serverIp, serverPort, sync.getUsername(), sync.getPassword());
                    CURRENT_SYNCS.put(sync.getLocalPath(), fileSyncOneShotProcessor); // 加入同步列表
                    fileSyncOneShotProcessor.syncFiles();// 执行同步
                    log.info("执行本地路径为 [{}] 的同步成功,耗时为 [{}] 秒", sync.getLocalPath(), (System.currentTimeMillis() - startTime) / 1000.0);
                } catch (IOException e) {
                    log.error("执行本地路径为 [{}] 的同步失败", sync.getLocalPath(), e);
                    CURRENT_SYNCS.get(sync.getLocalPath()).setSyncStatus(SyncStatus.SYNC_FAILED); // 设置同步状态为失败,这个状态会多线程访问的
                }

//            });

        }
    }
}
