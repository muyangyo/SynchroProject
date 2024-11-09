package com.example.raftdemo.model;

import com.example.raftdemo.config.LogEntry;
import com.example.raftdemo.config.RaftRole;

public class RaftNode {
    private String id;
    private String ip;
    private int port;
    private RaftRole role;
    private long currentTerm;
    private String votedFor;
    private List<LogEntry> log;
    private int commitIndex;
    private int lastApplied;
    private Map<String, Integer> nextIndex;
    private Map<String, Integer> matchIndex;
    private long lastHeartbeatTime;
    private long electionTimeout;


}