package com.muyang.blogsystem_spring.controller;


import com.muyang.blogsystem_spring.model.Comment;
import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.service.CommentService;
import com.muyang.blogsystem_spring.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
 * Time: 22:31
 */
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Autowired
    CommentService commentService;

    @RequestMapping("/updateComment")
    public Result updateComment(HttpServletRequest request, String comment, Integer blogId) {
        if (comment == null || !StringUtils.hasLength(comment)) {
            return Result.fail("非法评论!");
        }
        String token = request.getHeader("token");
        if (token == null) {
            return Result.fail("请先登录!");
        }
        return commentService.insertComment(token, comment, blogId);
    }

    @RequestMapping("/getCommentByBlogId")
    public List<Comment> getCommentByBlogId(Integer blogId) throws Exception {
        if (blogId == null || blogId <= 0) {
            return null;
        }
        return commentService.getCommentByBlogId(blogId);
    }
}
