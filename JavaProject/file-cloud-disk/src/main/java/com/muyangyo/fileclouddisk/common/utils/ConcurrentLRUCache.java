package com.muyangyo.fileclouddisk.common.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConcurrentLRUCache<K, V> {
    private final int capacity; // 缓存容量
    private final ConcurrentHashMap<K, CacheEntry<V>> cache; // 缓存
    private final ConcurrentLinkedDeque<K> queue; // 实现LRU算法的队列
    private final ScheduledExecutorService cleaner; // 清理器
    private final long timeout;
    private final TimeUnit timeUnit;

    public ConcurrentLRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.queue = new ConcurrentLinkedDeque<>();
        this.cleaner = null;
        this.timeout = 0;
        this.timeUnit = null;
    }

    public ConcurrentLRUCache(int capacity, long timeout, TimeUnit timeUnit) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>(capacity);
        this.queue = new ConcurrentLinkedDeque<>();
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::cleanup, timeout, timeout, timeUnit);
    }

    /**
     * 获取缓存的值
     *
     * @param key 缓存的键
     * @return 缓存的值
     */
    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry != null) {
            // 将访问的元素移到队列尾部
            queue.remove(key);
            queue.offer(key);
            return entry.value;
        }
        return null;
    }

    /**
     * 添加或更新缓存的值
     *
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public synchronized void put(K key, V value) {
        if (cache.containsKey(key)) {
            // 如果键已存在，更新值并将键移到队列尾部
            CacheEntry<V> entry = cache.get(key);
            entry.value = value;
            queue.remove(key);
            queue.offer(key);
        } else {
            if (cache.size() >= capacity) {
                // 如果缓存已满，移除最旧的元素
                K oldestKey = queue.poll();
                if (oldestKey != null) {
                    cache.remove(oldestKey);
                }
            }
            // 添加新元素到缓存和队列尾部
            cache.put(key, new CacheEntry<>(value, timeout, timeUnit));
            queue.offer(key);
        }
    }

    /**
     * 清理过期的缓存项
     */
    private synchronized void cleanup() {
        long currentTime = System.currentTimeMillis();
        for (K key : queue) {
            CacheEntry<V> entry = cache.get(key);
            if (entry != null && currentTime - entry.timestamp > timeUnit.toMillis(timeout)) {
                cache.remove(key);
                queue.remove(key);
            }
        }
    }

    /**
     * 关闭清理器
     */
    public synchronized void shutdown() {
        if (cleaner != null) {
            cleaner.shutdown();
        }
    }

    private static class CacheEntry<V> {
        private V value;
        private final long timestamp;
        private final long timeout;
        private final TimeUnit timeUnit;

        public CacheEntry(V value, long timeout, TimeUnit timeUnit) {
            this.value = value;
            this.timestamp = System.currentTimeMillis();
            this.timeout = timeout;
            this.timeUnit = timeUnit;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - timestamp > timeUnit.toMillis(timeout);
        }
    }
}