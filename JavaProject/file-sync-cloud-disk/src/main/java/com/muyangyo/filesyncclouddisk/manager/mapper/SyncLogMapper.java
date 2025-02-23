package com.muyangyo.filesyncclouddisk.manager.mapper;

import com.muyangyo.filesyncclouddisk.common.model.meta.SyncOperationLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/21
 * Time: 15:09
 */
@Mapper
public interface SyncLogMapper {
    int insert(SyncOperationLog log);

    List<SyncOperationLog> selectByCondition(SyncOperationLog condition);

    /**
     * 分页查询操作日志
     *
     * @return 操作日志列表
     */
    List<SyncOperationLog> selectAllWithLimit(int start, int size);

    /**
     * 删除所有操作日志
     *
     * @return 受影响的行数
     */
    Integer deleteAll();

    Integer getTotalCount();
}
