package com.muyangyo.filesyncclouddisk.user.mapper;

import com.muyangyo.filesyncclouddisk.common.model.meta.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper 接口定义了对 `users` 表的 CRUD 操作。
 */
@Mapper
public interface UserMapper {

    /**
     * 根据主键查询用户信息
     *
     * @param userId 用户ID
     * @return 用户对象
     */
    User selectByPrimaryKey(@Param("userId") String userId);

    User selectByUsername(String username);

    /**
     * 根据动态条件查询用户信息
     *
     * @param user 包含查询条件的用户对象
     * @return 符合条件的用户列表
     */
    List<User> selectByDynamicCondition(User user);

    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    int numberOfUsers();

    /**
     * 插入用户信息，如果字段为空则使用默认值
     *
     * @param user 用户对象
     * @return 插入成功的记录数
     */
    int insertSelective(User user);

    /**
     * 根据主键更新用户信息，动态更新字段
     *
     * @param user 用户对象
     * @return 更新成功的记录数
     */
    int updateByPrimaryKeySelective(User user);
}