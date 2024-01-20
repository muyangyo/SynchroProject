package com.muyang.blogsystem_spring.mapper;

import com.muyang.blogsystem_spring.model.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/19
 * Time: 13:31
 */
@SpringBootTest
class BlogMapperTest {

    @Autowired
    BlogMapper blogMapper;

    @Test
    void getBlogById() {
        System.out.println(blogMapper.selectBlogById(1));
    }

    @Test
    void getBlogCount() {
        System.out.println(blogMapper.selectBlogCount(1));
    }

    @Test
    void getAllBlogs() {
        System.out.println(blogMapper.selectAllBlogs());
    }

    @Test
    void insertBlog() {
        Blog blog = new Blog();
        blog.setTitle("测试文章2");
        blog.setContent("123123");
        blog.setUserId(1);
        blogMapper.insertBlog(blog);
        System.out.println(blogMapper.selectBlogById(3));
    }

    @Test
    void delBlog() {
        blogMapper.delBlog(2);
        System.out.println(blogMapper.selectAllBlogs());
    }

    @Test
    void updateBlog() {
        Blog blogNew = new Blog();
        blogNew.setTitle("测试文章3-1");
        blogNew.setContent("测试文章内容");
        blogNew.setId(3);
        blogMapper.updateBlog(blogNew);
    }
}