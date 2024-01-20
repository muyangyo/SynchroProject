package com.muyang.blogsystem_spring.mapper;

import com.muyang.blogsystem_spring.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/20
 * Time: 9:36
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentByBlogId(@Param("blogId") int blogId);

    int insertComment(@Param("comment") Comment comment);
}
