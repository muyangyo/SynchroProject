package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/15
 * Time: 22:37
 */
@Mapper
public interface UserXMLMapper {
    Integer insert1(User user);

    Integer insert2(User user);

    Integer insert3(@Param("user") User user1);


    Integer delete(@Param("user") User user1);


    void update(@Param("user") User user1);


    List<User> select1();


    List<User> select2();


    List<User> select3();

}
