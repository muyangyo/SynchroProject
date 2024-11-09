package com.example.raftdemo.raft;

import com.example.raftdemo.config.RaftRole;
import com.example.raftdemo.model.RaftNode;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/11/9
 * Time: 9:46
 */
public class LogReplication {

    private RaftNode node;

    public LogReplication(RaftNode node) {
        this.node = node;
    }

    public void replicateLog() {
        if (node.getRole() == RaftRole.LEADER) {
            for (RaftNode peer : getPeers()) {
                sendAppendEntries(peer);
            }
        }
    }

    private void sendAppendEntries(RaftNode peer) {
        // 发送AppendEntries消息到peer节点
    }
}