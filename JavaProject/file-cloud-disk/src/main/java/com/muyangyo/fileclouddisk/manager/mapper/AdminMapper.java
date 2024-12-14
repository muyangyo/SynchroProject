package com.muyangyo.fileclouddisk.manager.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    Admin selectByUserId(String userId);

    Admin selectByUsername(String username);

    Admin selectByDynamicCondition(Admin admin);

    Integer updateByUserId(Admin admin);

    Integer insertByDynamicCondition(Admin admin);

    Integer numberOfAdmin();
}