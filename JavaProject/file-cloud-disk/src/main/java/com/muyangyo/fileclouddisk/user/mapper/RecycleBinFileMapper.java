package com.muyangyo.fileclouddisk.user.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.RecycleBinFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/6
 * Time: 20:11
 */
@Mapper
public interface RecycleBinFileMapper {
    /**
     * 根据主键查询
     */
    RecycleBinFile selectByCode(String id);

    /**
     * 动态多条件查询
     */
    List<RecycleBinFile> selectByDynamicCondition(
            RecycleBinFile recycleBinFile);

    /**
     * 查询所有数据
     *
     * @return 所有数据
     */
    List<RecycleBinFile> selectAll();


    /**
     * 动态插入数据
     */
    int insertByDynamicCondition(RecycleBinFile recycleBinFile);

    /**
     * 根据主键删除数据
     */
    int deleteByCode(String id);

    /**
     * 删除所有数据
     */
    int deleteAll();
}
