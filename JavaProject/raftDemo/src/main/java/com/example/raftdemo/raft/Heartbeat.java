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
public class Heartbeat {

    private RaftNode node;

    public Heartbeat(RaftNode node) {
        this.node = node;
    }

    public void sendHeartbeat() {
        if (node.getRole() == RaftRole.LEADER) {
            for (RaftNode peer : getPeers()) {
                sendHeartbeat(peer);
            }
        }
    }

    private void sendHeartbeat(RaftNode peer) {
        // 发送心跳消息到peer节点
    }
}
