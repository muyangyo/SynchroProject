package com.muyang.blogsystem_spring.controller;

import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.service.BlogService;
import com.muyang.blogsystem_spring.tools.TokenTool;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    @RequestMapping("/addBlog")
    public Result addBlog(String title, String content, HttpServletRequest request) {
        if (!StringUtils.hasLength(title) || !StringUtils.hasLength(content)) {
            return Result.fail("非法文章输入!");
        }
        String token = request.getHeader("token");
        if (token == null) {
            return Result.fail("请先登录!");

        }
        Claims map = TokenTool.getMapFromToken(token);
        int userId = Integer.parseInt(map.get("userId").toString());

        Blog blog = new Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(userId);
        return blogService.addBlog(blog);
    }

    @RequestMapping("/updateBlog")
    public Result updateBlog(String title, String content, Integer userId, Integer blogId, HttpServletRequest request) {
        if (!StringUtils.hasLength(title) || !StringUtils.hasLength(content) || userId == null || blogId == null) {
            return Result.fail("非法文章输入!");
        }
        //验证是否是作者本人
        String token = request.getHeader("token");
        if (token == null) {
            return Result.fail("请先登录!");

        }
        Claims map = TokenTool.getMapFromToken(token);
        int curUserId = Integer.parseInt(map.get("userId").toString());
        if (curUserId != userId) {
            return Result.fail("非作者本人,无法修改!");
        }

        Blog blog = new Blog();
        blog.setContent(content);
        blog.setTitle(title);
        blog.setId(blogId);
        return blogService.updateBlog(blog);
    }
}
