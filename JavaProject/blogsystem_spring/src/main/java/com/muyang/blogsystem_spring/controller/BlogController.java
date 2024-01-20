package com.muyang.blogsystem_spring.controller;

import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 10:19
 */
@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    @Autowired
    BlogService blogService;

    @RequestMapping("/getBlogList")
    public List<Blog> getBlogList() {
        return blogService.getBlogList();
    }

    @RequestMapping("/getBlog")
    public Blog getBlog(Integer blogId) {
        if (blogId == null) {
            log.info("非法文章ID");
            return null;
        }
        return blogService.getBlog(blogId);
    }

    @RequestMapping("/delBlog")
    public Boolean delBlog(HttpServletRequest httpServletRequest, Integer blogId) throws Exception {
        if (blogId == null) {
            log.info("非法文章ID");
            return false;
        }
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            return false;
        }
        return blogService.delBlog(token, blogId);
    }


}
