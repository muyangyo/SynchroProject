package com.example.demo240115.service;

import com.example.demo240115.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Transactional
    public Integer insertUser(String userName, String password) {
        return userInfoMapper.insert(userName, password);
    }


}
