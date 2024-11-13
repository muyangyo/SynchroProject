package com.muyangyo.filesyncclouddisk.utils;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;

public class CacheExample {
    public static void main(String[] args) {
        // 创建一个带有过期时间的缓存，最大容量为 3，每个缓存项的过期时间为 5 秒
        Cache<String, String> timedCache = CacheUtil.newTimedCache(5000);

        // 添加缓存项
        timedCache.put("key1", "value1");
        timedCache.put("key2", "value2");
        timedCache.put("key3", "value3");

        // 等待 6 秒，缓存项将过期
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 输出缓存内容
        System.out.println(timedCache.get("key1")); // null，因为 key1 已过期
        System.out.println(timedCache.get("key2")); // null，因为 key2 已过期
        System.out.println(timedCache.get("key3")); // null，因为 key3 已过期
    }
}