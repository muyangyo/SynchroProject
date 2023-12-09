package com.muyang.booksystem.service;

import com.muyang.booksystem.mapper.BookMapper;
import com.muyang.booksystem.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:13
 */
@Service
public class BookServer {
    @Autowired
    private BookMapper bookMapper;

    public List<Book> getBookList() {
        //数据层
        List<Book> bookList = bookMapper.selectAll();
        //业务逻辑层
        for (Book b : bookList) {
            if (b.getStatus() != 1) {
                bookList.remove(b);
            }
        }
        //表现层
        return bookList;
    }
}
