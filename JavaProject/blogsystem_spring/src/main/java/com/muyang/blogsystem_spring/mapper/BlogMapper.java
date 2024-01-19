package com.muyang.blogsystem_spring.mapper;

import com.muyang.blogsystem_spring.model.Blog;
import com.muyang.blogsystem_spring.model.User;
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

    Blog getBlogById(@Param("blogId") int blogId);

    int getBlogCount(@Param("userId") int userId);

    List<Blog> getAllBlogs();

    int insertBlog(@Param("blog") Blog blog);

    int delBlog(@Param("blogId") int blogId);

    int updateBlog(@Param("blog") Blog blog);
}
