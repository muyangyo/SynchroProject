package com.muyang.booksystem.service;

import com.muyang.booksystem.dao.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:13
 */
public class BookServer {
    public List<Book> getBookList() {
        //数据层
        List<Book> ret = MockBookList(); //先弄个假数据
        //业务逻辑层
        for (Book b : ret) {
            if (b.getRemain() > 0) {
                b.setRemainCN("可借阅");
            } else {
                b.setRemainCN("不可借阅");
            }
        }
        //表现层
        return ret;
    }

    public List<Book> MockBookList() {
        List<Book> ret = new ArrayList<>(10);
        for (int i = 1; i < 11; i++) {
            Book book;
            if (i % 5 == 0) {
                book = new Book(i, "程序员的自我修养", "root", 20, 3300, "root", 0);
            } else {
                book = new Book(i, "程序员的自我修养", "root", 20, 3300, "root", 1);
            }
            ret.add(book);
        }
        return ret;
    }
}
