package com.muyangyo.fileclouddisk.manager.mapper;

import com.muyangyo.fileclouddisk.common.model.meta.OperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OperationLogMapper {
    /**
     * 动态多条件查询操作日志
     *
     * @param params 查询参数
     * @return 操作日志列表
     */
    List<OperationLog> selectByConditions(OperationLog operationLog);

    /**
     * 插入操作日志，字段为空时使用默认值
     *
     * @param operationLog 操作日志对象
     * @return 受影响的行数
     */
    Integer insertOperationLog(OperationLog operationLog);

    /**
     * 分页查询操作日志
     *
     * @param offset 起始记录数
     * @param limit  每页记录数
     * @return 操作日志列表
     */
    List<OperationLog> selectAllWithLimit(int start, int size);

    /**
     * 删除所有操作日志
     *
     * @return 受影响的行数
     */
    Integer deleteAll();

    Integer getTotalCount();

}