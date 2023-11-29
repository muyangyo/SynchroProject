package com.example.scdemo231128.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/11/29
 * Time: 13:15
 */
@Repository
@Mapper
public interface FileMapper {
    Integer insert(@Param("fileName") String fileName, @Param("path") String path);
}
