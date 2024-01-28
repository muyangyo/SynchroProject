package com.muyang.mq.common.demo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/28
 * Time: 12:17
 */
@Data
public class User implements Serializable {
    public static int a = 10;
    private int b = 10;
}
