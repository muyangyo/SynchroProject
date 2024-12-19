package com.muyangyo.fileclouddisk.manager.service;

import com.muyangyo.fileclouddisk.common.model.enums.OperationLevel;
import com.muyangyo.fileclouddisk.common.model.meta.OperationLog;
import com.muyangyo.fileclouddisk.common.model.vo.OperationLogListVO;
import com.muyangyo.fileclouddisk.common.model.vo.OperationLogVO;
import com.muyangyo.fileclouddisk.common.utils.EasyTimer;
import com.muyangyo.fileclouddisk.common.utils.NetworkUtils;
import com.muyangyo.fileclouddisk.common.utils.TokenUtils;
import com.muyangyo.fileclouddisk.manager.mapper.OperationLogMapper;
import converter.MetaToVoConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
@Slf4j
public class OperationLogService {
    @Resource
    private OperationLogMapper operationLogMapper;

    /**
     * 添加操作日志
     *
     * @param operation 操作
     * @param username  用户名
     * @param ip        IP
     * @param level     等级
     */
    public void addLog(String operation, String username, String ip, OperationLevel level) {
        OperationLog operationLog = new OperationLog(new Date(), operation, username, ip, level);
        operationLogMapper.insertOperationLog(operationLog);
        log.trace("用户操作日志[ 操作: {} , 用户名: {} , IP: {} , 等级: {} ]", operation, username, ip, level);
    }

    /**
     * 添加操作日志（从request中获取用户名和ip）
     *
     * @param operation 操作
     * @param level     等级
     */
    public void addLogFromRequest(String operation, OperationLevel level, HttpServletRequest request) {
        String token = TokenUtils.getTokenFromCookie(request);
        TokenUtils.TokenLoad tokenLoad = TokenUtils.TokenLoad.fromMap(TokenUtils.getTokenLoad(token));
        String username = tokenLoad.getUsername();
        String ip = NetworkUtils.getClientIp(request);
        OperationLog operationLog = new OperationLog(new Date(), operation, username, ip, level);
        operationLogMapper.insertOperationLog(operationLog);
        log.trace("用户操作日志[ 操作: {} , 用户名: {} , IP: {} , 等级: {} ]", operation, username, ip, level);
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
