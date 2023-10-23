package com.demo.springdemo231023.RequestMappingDemo;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/23
 * Time: 17:59
 */
public class Person {
    public Integer id;
    public String name;

    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
