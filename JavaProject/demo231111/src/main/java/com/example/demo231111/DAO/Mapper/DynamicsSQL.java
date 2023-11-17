package com.example.demo231111.DAO.Mapper;

import com.example.demo231111.DAO.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/17
 * Time: 21:57
 */
@Mapper
public interface DynamicsSQL {
    void insert(User user);

    void delete(@Param("ids") List<Integer> ids1);

    User select(User user);

    void update(User user);
}
