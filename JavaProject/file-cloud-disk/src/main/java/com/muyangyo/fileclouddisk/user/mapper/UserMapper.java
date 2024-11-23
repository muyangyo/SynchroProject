package com.muyangyo.fileclouddisk.user.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByUserId(String userId);

    User selectByUsername(String username);

    User selectByDynamicCondition(User user);

    Integer insertByDynamicCondition(User user);

    Integer updateByUserId(User user);

    Integer numberOfUser();
}