package com.muyangyo.fileclouddisk.user.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.ShareFile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

@Mapper
public interface ShareFileMapper {

    // 根据主键查询
    ShareFile selectByCode(String code);

    // 动态多条件查询
    LinkedList<ShareFile> selectByDynamicCondition(ShareFile shareFile);

    // 插入数据，如果数据为空则使用默认值
    int insertByDynamicCondition(ShareFile shareFile);

    // 动态更新数据
    int update(ShareFile shareFile);

    // 删除数据
    int deleteByCode(String code);

    List<ShareFile> selectByDynamicConditionAndLimit(@Param("shareFile") ShareFile shareFile, int start, int size);

    int getShareFileCount(ShareFile shareFile);

    int deleteAllByCreator(String creator);

    int deleteExpiredShareFileByCreator(String creator);

    List<ShareFile> selectAll();
}