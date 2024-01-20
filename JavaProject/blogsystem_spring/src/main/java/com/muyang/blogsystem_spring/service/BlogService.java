package com.muyang.blogsystem_spring.service;

import com.muyang.blogsystem_spring.mapper.BlogMapper;
import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.tools.TokenTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 10:18
 */
@Service
@Slf4j
public class BlogService {
    @Autowired
    BlogMapper blogMapper;

    public List<Blog> getBlogList() {
        log.info("获取全部博客成功!");
        return blogMapper.selectAllBlogs();
    }

    public Blog getBlog(int blogId) {
        Blog blog = blogMapper.selectBlogById(blogId);
        if (blog == null) {
            log.warn("没有id= {} 的文章!", blogId);
            return null;
        }
        log.info("获取文章成功 " + blog);
        return blog;
    }

    public boolean delBlog(String token, Integer blogId) throws Exception {
        Claims map = TokenTool.getMapFromToken(token);
        if (map == null) {
            throw new Exception("无效token");
        }
        int curUserId = Integer.parseInt((String) map.get("userId"));//当前账户id
        Blog blog = blogMapper.selectBlogById(blogId);
        //如果不是作者本人,则返回false
        if (blog.getUserId() != curUserId) {
            return false;
        }
        //是作者本人,进行删除操作
        int i = blogMapper.delBlog(blogId);
        if (i != 1) {
            throw new Exception("删除博客失败");
        }
        return true;
    }
}
