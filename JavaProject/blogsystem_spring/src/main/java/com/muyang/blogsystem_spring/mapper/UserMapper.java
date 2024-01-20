package com.muyang.blogsystem_spring.mapper;

import com.muyang.blogsystem_spring.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/19
 * Time: 12:20
 */
@Mapper
public interface UserMapper {
    User selectUserById(@Param("id") int id);

    User selectUserByName(@Param("userName") String userName);

    List<User> selectAllUsers();

    int insertUsers(@Param("user") User user);

}
