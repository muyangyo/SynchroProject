package com.muyangyo.fileclouddisk.common.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/23
 * Time: 12:56
 */
@Mapper
public interface CreateTablesMapper {
    Integer createSharedDirectoryTable(); // 创建共享目录表

    Integer CreateAdminTable(); // 创建管理员表

    Integer CreateUserTable(); // 创建用户表

//    Integer BlackListIPTable(); // 创建黑名单IP表
}
