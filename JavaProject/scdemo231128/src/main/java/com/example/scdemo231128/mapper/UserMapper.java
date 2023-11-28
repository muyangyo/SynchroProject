package com.example.scdemo231128.mapper;

import com.example.scdemo231128.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/28
 * Time: 23:13
 */
@Repository
@Mapper
public interface UserMapper {
    User selectUserById(String username);
}
