package com.muyangyo.filesyncclouddisk.common.mapper.wrapped;

import com.muyangyo.filesyncclouddisk.common.model.meta.MountDir;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MountDirMapper {
    MountDir select();

    void insert(MountDir mountDir);

    void update(MountDir mountDir);
}