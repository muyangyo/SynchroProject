package com.muyangyo.filesyncclouddisk.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/13
 * Time: 19:28
 */
public class RandomString {
    public static void main(String[] args) {
        String adminKey = RandomUtil.randomString(10);
        System.out.println(adminKey);
    }
}
