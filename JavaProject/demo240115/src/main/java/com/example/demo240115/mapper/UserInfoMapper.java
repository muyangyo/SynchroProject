package com.example.demo240115.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {
    @Insert("insert into user_info (user_name,password) values( #{userName} , #{password} )")
    Integer insert(@Param("userName") String userName, @Param("password") String password);
}
