package com.muyangyo.fileclouddisk.manager.service;

import com.muyangyo.fileclouddisk.manager.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;
}