package com.muyang.booksystem.controller;

import com.muyang.booksystem.model.Book;
import com.muyang.booksystem.service.BookServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/30
 * Time: 14:33
 */
@RequestMapping("/BookController")
@RestController
public class BookController {
    @Autowired
    private BookServer bookServer;

    @RequestMapping("/retBookList")
    public List<Book> retBookList() {
        List<Book> ret = bookServer.getBookList();
        System.out.println(ret);
        return ret;
    }
}
