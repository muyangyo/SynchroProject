package com.muyangyo.filesyncclouddisk.common.utils;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/14
 * Time: 13:09
 */

public class EasyTimedCache<K, V> {
    private final Map<K, CacheEntry<V>> cache; // 缓存
    private final int capacity; // 容量
    private final long timeout; // 超时时间
    private final ScheduledExecutorService scheduler; // 超时清理器

    public EasyTimedCache(int capacity, long timeout, boolean useSchedulePrune) {
        this.capacity = capacity;
        this.timeout = timeout;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        if (useSchedulePrune) {
            this.scheduler.scheduleAtFixedRate(this::pruneExpiredEntries, timeout, timeout + timeout >> 1, TimeUnit.MILLISECONDS);// 启动定时清理器
        }
    }

    /**
     * 添加缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return true or false
     */
    public synchronized boolean put(K key, V value) {
        if (cache.size() < capacity) {
            cache.put(key, new CacheEntry<>(value, timeout));
            return true;
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param key 缓存键
     * @return 缓存值 or null
     */
    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        return null;
    }

    /**
     * 未过期的缓存数量
     *
     * @return 数量
     */
    public synchronized int size() {
        return (int) cache.values().stream().filter(entry -> !entry.isExpired()).count();
    }

    /**
     * 清理过期的缓存项
     */
    private void pruneExpiredEntries() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    /**
     * 缓存项类
     */
    private static class CacheEntry<V> {
        private final V value;
        private final long expirationTime;

        public CacheEntry(V value, long timeout) {
            this.value = value;
            this.expirationTime = System.currentTimeMillis() + timeout;
        }

        public V getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }

    /**
     * 关闭调度器
     */
    public void shutdown() {
        scheduler.shutdown();
    }
}