package com.muyangyo.fileclouddisk.common.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.ShareDir;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareDirMapper {
    ShareDir select();

    void insert(ShareDir shareDir);

    void update(ShareDir shareDir);
}