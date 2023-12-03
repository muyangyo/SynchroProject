package com.example.aop.model;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/12/3
 * Time: 21:29
 */
@Data
public class Demo {
    private String msg;
    private Integer integer;

    public Demo(String msg, Integer integer) {
        this.msg = msg;
        this.integer = integer;
    }
}
