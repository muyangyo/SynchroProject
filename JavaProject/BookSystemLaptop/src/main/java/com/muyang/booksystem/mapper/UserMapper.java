package com.muyang.booksystem.mapper;

import com.muyang.booksystem.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/20
 * Time: 16:56
 */
@Mapper
public interface UserMapper {

    List<User> selectAll();

    User selectOne(User user);


}
