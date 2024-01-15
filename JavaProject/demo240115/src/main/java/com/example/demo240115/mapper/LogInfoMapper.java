package com.example.demo240115.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LogInfoMapper {
    @Insert("insert into log_info (user_name,op) values(#{userName},#{op})")
    Integer insert(@Param("userName") String userName, @Param("op") String op);
}
