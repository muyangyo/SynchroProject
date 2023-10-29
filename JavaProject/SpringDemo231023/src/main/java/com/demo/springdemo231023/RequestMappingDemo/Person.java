package com.demo.springdemo231023.RequestMappingDemo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/23
 * Time: 17:59
 */
public class Person {
    @Getter @Setter
    private Integer id;
    private String name;
}
