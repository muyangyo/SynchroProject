package com.muyang.blogsystem_spring.service;

import com.muyang.blogsystem_spring.mapper.BlogMapper;
import com.muyang.blogsystem_spring.mapper.CommentMapper;
import com.muyang.blogsystem_spring.mapper.UserMapper;
import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.model.Comment;
import com.muyang.blogsystem_spring.model.Result;
import com.muyang.blogsystem_spring.model.User;
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
 * Time: 22:32
 */
@Service
@Slf4j
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    UserMapper userMapper;

    public Result insertComment(String token, String content, int blogId) {
        //看看这个博客存在不存在
        Blog blog = blogMapper.selectBlogById(blogId);
        if (blog == null) {
            return Result.fail("不存在id为 {} 的博客", blogId);
        }
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setContent(content);

        Claims map = TokenTool.getMapFromToken(token);
        int userId = Integer.parseInt((String) map.get("userId"));
        User user = userMapper.selectUserById(userId);
        comment.setUserName(user.getUserName());

        log.info("id为 {} 的博客,添加了一个评论:' {} '", comment.getBlogId(), comment.getContent());
        commentMapper.insertComment(comment);
        return Result.success("成功添加评论!");
    }

    public List<Comment> getCommentByBlogId(int blogId) throws Exception {
        Blog blog = blogMapper.selectBlogById(blogId);
        if (blog == null) {
            throw new Exception("不存在id为 " + blogId + " 的博客");
        }
        log.info("获取id为 {} 的文章评论成功!", blogId);
        return commentMapper.selectCommentByBlogId(blogId);
    }
}
