package com.muyang.booksystem.dao;

import lombok.Data;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:08
 */
@Data
public class Book {
    private Integer id;
    private String name;
    private String author;
    private Integer count;//数量
    private long price;//规定该单位为: 分
    private String publish;
    private Integer remain;//剩余数量 > 0 可借阅
    private String remainCN;//可否借阅的中文

    public Book(Integer id, String name, String author, Integer count, long price, String publish, Integer remain) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.count = count;
        this.price = price;
        this.publish = publish;
        this.remain = remain;
    }

    public Book() {
    }
}
