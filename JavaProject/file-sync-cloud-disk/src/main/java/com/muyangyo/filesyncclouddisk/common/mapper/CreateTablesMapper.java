package com.muyangyo.filesyncclouddisk.common.mapper;

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
    Integer createMountDirsTable(); // 创建挂载目录表

    Integer createAdminTable(); // 创建管理员表

    Integer createUserTable(); // 创建用户表

    Integer createShareFileTable(); // 创建分享文件表

    Integer createOperationLogTable(); // 创建操作日志表

    Integer createRecycleBinTable(); // 创建回收站表

//    Integer BlackListIPTable(); // 创建黑名单IP表
}
