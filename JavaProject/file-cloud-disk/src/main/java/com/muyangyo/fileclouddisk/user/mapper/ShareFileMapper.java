package com.muyangyo.fileclouddisk.user.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.LinkedList;

@Mapper
public interface ShareFileMapper {

    // 根据主键查询
    ShareFile selectById(String id);

    // 动态多条件查询
    LinkedList<ShareFile> selectByDynamicCondition(ShareFile shareFile);

    // 插入数据，如果数据为空则使用默认值
    int insertByDynamicCondition(ShareFile shareFile);

    // 动态更新数据
    int update(ShareFile shareFile);

    // 删除数据
    int deleteById(String id);
}