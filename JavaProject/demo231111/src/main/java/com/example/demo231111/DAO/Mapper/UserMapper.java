package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/11
 * Time: 18:25
 */
@Mapper
public interface UserMapper {


    @Select("SELECT * FROM userinfo where id = #{id}")
    List<User> returnAll(Integer id);

    @Select("SELECT * FROM userinfo where id = #{id}")
    List<User> returnAll1(Integer i);


    @Select("SELECT * FROM userinfo where id = #{id}")
    List<User> returnAll2(@Param("id") Integer userId);


    @Insert("insert into userinfo (username,password,age) values (#{username},#{password},#{age})")
    Integer insert1(User user);


    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into userinfo (username,password,age) values (#{username},#{password},#{age})")
    Integer insert2(User user);

    @Insert("insert into userinfo (username,password,age) values (#{user.username},#{user.password},#{user.age})")
    Integer insert3(@Param("user") User user1);


}
