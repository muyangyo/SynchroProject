package com.example.raftdemo.raft;

import com.example.raftdemo.config.RaftRole;
import com.example.raftdemo.model.RaftNode;

public class LogCompaction {

    private RaftNode node;

    public LogCompaction(RaftNode node) {
        this.node = node;
    }

    public void compactLog() {
        if (node.getRole() == RaftRole.LEADER) {
            // 压缩已提交的日志
            createSnapshot();
            // 删除旧的日志条目
            deleteOldLogs();
        }
    }

    private void createSnapshot() {
        // 创建快照
    }

    private void deleteOldLogs() {
        // 删除旧的日志条目
    }
}