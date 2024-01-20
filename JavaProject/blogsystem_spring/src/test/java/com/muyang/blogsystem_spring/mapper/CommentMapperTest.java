package com.muyang.blogsystem_spring.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 9:43
 */
@SpringBootTest
class CommentMapperTest {
    @Autowired
    CommentMapper commentMapper;

    @Test
    void selectCommentByBlogId() {
        commentMapper.selectCommentByBlogId(1);
    }
}