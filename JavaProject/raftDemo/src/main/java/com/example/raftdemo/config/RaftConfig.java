package com.example.raftdemo.config;

import java.util.List;
import java.util.Map;

public class RaftConfig {
    public static final int ELECTION_TIMEOUT_MIN = 150;  // 选举超时时间最小值（毫秒）
    public static final int ELECTION_TIMEOUT_MAX = 300;  // 选举超时时间最大值（毫秒）
    public static final int HEARTBEAT_INTERVAL = 50;     // 心跳间隔时间（毫秒）
}

enum RaftRole {
    LEADER, FOLLOWER, CANDIDATE
}



class LogEntry {
    private long term;
    private String command;
}