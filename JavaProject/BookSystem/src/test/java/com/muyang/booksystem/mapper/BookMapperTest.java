package com.muyang.booksystem.mapper;

import com.muyang.booksystem.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/20
 * Time: 17:18
 */
@SpringBootTest
class BookMapperTest {

    @Autowired
    BookMapper bookMapper;

    @Test
    void selectAll() {
        System.out.println(bookMapper.selectAll().toString());
    }

    @Test
    void selectOne() {
        Book book = new Book();
        book.setId(1);
//        book.setBookName("西游记");
//        book.setAuthor("作者");
        System.out.println(bookMapper.selectOne(book).toString());
    }
}