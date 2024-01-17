package com.example.demo240115.service;

import com.example.demo240115.mapper.LogInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
public class LogService {
    @Autowired
    private LogInfoMapper logInfoMapper;

    @Transactional(propagation = Propagation.NESTED)
    public Integer insertLog(String userName, String op) {
        logInfoMapper.insert(userName, op);
        try {
            int a = 10 / 0;
        } catch (Exception e) {
            log.info(e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动回滚
        }

        return 1;
    }
}
