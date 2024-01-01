package com.muyang.globaltemplate.global.mapper;

import com.muyang.globaltemplate.global.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/1
 * Time: 16:56
 */
@Mapper
public interface UserMapper {
    User getByUsername(@Param("user") User user);
}
