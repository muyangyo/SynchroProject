package com.muyangyo.filesyncclouddisk.common.service;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/13
 * Time: 18:51
 */

public class CacheService {
    // 创建一个带有过期时间的缓存，最大容量为 3，每个缓存项的过期时间为 5 分钟
    public final static Cache<String, String> timedCache = CacheUtil.newTimedCache(5 * 60 * 1000);
    // 创建一个 FIFO 缓存，最大容量为 5
    public final static Cache<String, String> fifoCache = CacheUtil.newFIFOCache(5);
}
