package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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

}
