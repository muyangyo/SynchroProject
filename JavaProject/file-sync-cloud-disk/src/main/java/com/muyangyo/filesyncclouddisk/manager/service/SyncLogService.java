package com.muyangyo.filesyncclouddisk.manager.service;

import com.muyangyo.filesyncclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.filesyncclouddisk.common.model.meta.SyncOperationLog;
import com.muyangyo.filesyncclouddisk.manager.mapper.SyncLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2025/2/23
 * Time: 16:55
 */
@Service
@Slf4j
public class SyncLogService {
    @Resource
    private SyncLogMapper syncLogMapper;

    public static final String ADD = "更新或上传";
    public static final String DELETE = "删除";
    public static final String DOWNLOAD = "获取";

    public void addLog(String operation, String targetFileName, String syncName, String ip, OperationLevel level) {
        SyncOperationLog syncOperationLog = new SyncOperationLog(null, new Date(), operation, targetFileName, syncName, ip, level);
        syncLogMapper.insert(syncOperationLog);
        log.trace("同步日志 [ 操作: {} 目标文件: {} 同步名称: {} IP: {} 级别: {} ]", operation, targetFileName, syncName, ip, level);
    }

/*    public OperationLogListVO getLogList(int currentPageIndex, int pageSize) {
        List<SyncOperationLog> syncOperationLogs = syncLogMapper.selectAllWithLimit(currentPageIndex * pageSize, pageSize);

        ArrayList<OperationLogVO> ret = new ArrayList<>(cloudDiskOperationLogList.size());
        for (CloudDiskOperationLog o : cloudDiskOperationLogList) {
            OperationLogVO convert = MetaToVoConverter.convert(o, OperationLogVO.class);
            convert.setOperationTime(EasyTimer.getFormatTime(o.getOperationTime()));
            ret.add(convert);
        }

        int totalCount = syncLogMapper.getTotalCount();
        return new OperationLogListVO(pageSize, totalCount, ret);
    }*/

    public boolean deleteAllLog() {
        syncLogMapper.deleteAll();
        return true;
    }
}
