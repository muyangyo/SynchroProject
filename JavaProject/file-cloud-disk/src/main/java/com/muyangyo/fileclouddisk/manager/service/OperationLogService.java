package com.muyangyo.fileclouddisk.manager.service;

import com.muyangyo.fileclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.fileclouddisk.common.model.meta.OperationLog;
import com.muyangyo.fileclouddisk.common.model.vo.OperationLogListVO;
import com.muyangyo.fileclouddisk.common.model.vo.OperationLogVO;
import com.muyangyo.fileclouddisk.common.utils.EasyTimer;
import com.muyangyo.fileclouddisk.manager.mapper.OperationLogMapper;
import converter.MetaToVoConverter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/12/18
 * Time: 15:29
 */
@Service
public class OperationLogService {
    @Resource
    private OperationLogMapper operationLogMapper;

    public boolean addLog(String operation, String username, String ip, OperationLevel level) {
        OperationLog operationLog = new OperationLog(new Date(), operation, username, ip, level);
        operationLogMapper.insertOperationLog(operationLog);
        return true;
    }

    @SneakyThrows
    public OperationLogListVO getLogList(int currentPageIndex, int pageSize) {
        List<OperationLog> operationLogList =
                operationLogMapper.selectAllWithLimit(currentPageIndex * pageSize, pageSize);

        ArrayList<OperationLogVO> ret = new ArrayList<>(operationLogList.size());
        for (OperationLog o : operationLogList) {
            OperationLogVO convert = MetaToVoConverter.convert(o, OperationLogVO.class);
            convert.setOperationTime(EasyTimer.getFormatTime(o.getOperationTime()));
            ret.add(convert);
        }
        int totalCount = operationLogMapper.getTotalCount();
        return new OperationLogListVO(pageSize, totalCount, ret);
    }

    public boolean deleteAllLog() {
        operationLogMapper.deleteAll();
        return true;
    }
}
