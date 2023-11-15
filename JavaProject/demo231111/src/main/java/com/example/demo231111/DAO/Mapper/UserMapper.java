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


    @Delete("delete from userinfo where id = #{user.id}")
    Integer delete(@Param("user") User user1);


    @Update("update userinfo set age = #{user.age} where id = #{user.id}")
    void update(@Param("user") User user1);


    @Select("select id,username,password,age,gender,phone," +
            "delete_flag as deleteFlag," +
            "create_time as createTime," +
            "update_time as updateTime " +
            "from userinfo")
    List<User> select1();

    @Results(id = "defaultMapper", value = {
            @Result(column = "delete_flag", property = "deleteFlag"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime"),
    })
    @Select("select id,username,password,age,gender,phone," +
            "delete_flag,create_time,update_time from userinfo")
    List<User> select2();


    @Select("select id,username,password,age,gender,phone," +
            "delete_flag,create_time,update_time from userinfo")
    List<User> select3();
}
