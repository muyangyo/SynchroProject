package com.muyangyo.filesyncclouddisk.utils;

import com.muyangyo.filesyncclouddisk.common.utils.EasyTimedCache;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EasyTimedCacheTest {
    private EasyTimedCache<String, String> cache;

    @BeforeEach
    public void setUp() {
        // 初始化缓存，容量为2，超时为100毫秒，启用定时清理器
        cache = new EasyTimedCache<>(2, 100, true);
    }

    @AfterEach
    public void tearDown() {
        // 清理缓存
        cache.shutdown();
    }

    @Test
    public void testPutAndGet() {
        assertTrue(cache.put("key1", "value1"));
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testPutWhenFull() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        // 当前缓存已满，添加第三个应返回 false
        assertFalse(cache.put("key3", "value3"));
    }

    @Test
    public void testExpiration() throws InterruptedException {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));

        // 等待超时
        Thread.sleep(150);
        assertNull(cache.get("key1")); // 应该返回 null 因为缓存已过期
    }

}
