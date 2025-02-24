package com.muyangyo.filesyncclouddisk.manager.mapper;

import com.muyangyo.filesyncclouddisk.common.model.meta.SyncInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SyncInfoMapper {
    /**
     * 查询所有同步信息
     *
     * @return 同步信息列表
     */
    List<SyncInfo> selectAll();

    /**
     * 根据主键查询同步信息
     *
     * @param name FTPS用户名（主键,也可以称为同步名）
     * @return 同步信息对象
     */
    SyncInfo selectByName(String name);

    /**
     * 查询所有激活的同步信息
     * @return 激活的同步信息列表
     */
    List<SyncInfo> selectActive();

    /**
     * 多条件动态查询同步信息
     *
     * @param condition 查询条件对象
     * @return 符合条件的同步信息列表
     */
    List<SyncInfo> selectByCondition(SyncInfo condition);

    /**
     * 插入同步信息（动态处理空值）
     *
     * @param syncInfo 同步信息对象
     * @return 受影响的行数
     */
    int insert(SyncInfo syncInfo);

    /**
     * 根据用户名(同步名)动态更新同步信息
     *
     * @param syncInfo 同步信息对象
     * @return 受影响的行数
     */
    int updateByName(SyncInfo syncInfo);

    /**
     * 根据用户名(同步名)动态更新最后同步时间
     * @param syncName 同步名
     * @param lastSyncTime 最后同步时间
     * @return 受影响的行数
     */
    int updateLastSyncTimeByName(@Param("username") String syncName, Date lastSyncTime);

    /**
     * 根据主键删除同步信息
     *
     * @param name FTPS用户名（主键）
     * @return 受影响的行数
     */
    int deleteByName(String name);

    /**
     * 删除所有同步信息
     *
     * @return 受影响的行数
     */
    int deleteAll();

    /**
     * 获取同步信息总数
     * @return 同步信息总数
     */
    int getCount();
}