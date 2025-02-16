package com.muyangyo.filesyncclouddisk.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CustomTimer {

    private final ScheduledExecutorService scheduler;

    public CustomTimer() {
        // 创建一个单线程的 ScheduledExecutorService
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 在指定的延迟后执行任务
     *
     * @param task  要执行的任务
     * @param delay 延迟时间（单位：秒）
     */
    public void scheduleTask(Runnable task, long delay) {
        scheduler.schedule(task, delay, TimeUnit.SECONDS);
    }

    /**
     * 定期执行任务
     *
     * @param task         要执行的任务
     * @param initialDelay 初始延迟时间（单位：秒）
     * @param period       执行周期（单位：秒）
     */
    public void scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
        scheduler.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
    }

    /**
     * 关闭定时器
     */
    public void shutdown() {
        scheduler.shutdown();
    }

    /**
     * 立即关闭定时器，并尝试停止所有正在执行的任务
     */
    public void shutdownNow() {
        scheduler.shutdownNow();
    }

    /**
     * 等待定时器关闭
     *
     * @param timeout 超时时间（单位：秒）
     * @return 是否成功关闭
     */
    public boolean awaitTermination(long timeout) throws InterruptedException {
        return scheduler.awaitTermination(timeout, TimeUnit.SECONDS);
    }
}