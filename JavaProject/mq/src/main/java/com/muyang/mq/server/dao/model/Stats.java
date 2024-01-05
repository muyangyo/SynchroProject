package com.muyang.mq.server.dao.model;

import lombok.Data;

@Data
//统计信息文件对象
public class Stats {
    private int totalCount;  // 总消息数量
    private int validCount;  // 有效消息数量
}