package com.muyang.blogsystem_spring.mapper;

import com.muyang.blogsystem_spring.model.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/19
 * Time: 12:34
 */
@Mapper
public interface BlogMapper {

    Blog selectBlogById(@Param("blogId") int blogId);

    int selectBlogCount(@Param("userId") int userId);

    List<Blog> selectAllBlogs();

    int insertBlog(@Param("blog") Blog blog);

    int delBlog(@Param("blogId") int blogId);

    int updateBlog(@Param("blog") Blog blog);
}
