
package com.example.raftdemo.raft;

import com.example.raftdemo.config.LogEntry;
import com.example.raftdemo.config.RaftRole;
import com.example.raftdemo.model.RaftNode;

public class MessageHandler {

    private RaftNode node;

    public MessageHandler(RaftNode node) {
        this.node = node;
    }

    public void handleRequestVote(RequestVoteRequest request, RequestVoteResponse response) {
        if (request.getTerm() > node.getCurrentTerm()) {
            node.setCurrentTerm(request.getTerm());
            node.setRole(RaftRole.FOLLOWER);
        }

        if (request.getTerm() == node.getCurrentTerm() &&
                (node.getVotedFor() == null || node.getVotedFor().equals(request.getCandidateId())) &&
                isLogUpToDate(request.getLastLogTerm(), request.getLastLogIndex())) {
            node.setVotedFor(request.getCandidateId());
            response.setVoteGranted(true);
        } else {
            response.setVoteGranted(false);
        }
    }

    public void handleAppendEntries(AppendEntriesRequest request, AppendEntriesResponse response) {
        if (request.getTerm() > node.getCurrentTerm()) {
            node.setCurrentTerm(request.getTerm());
            node.setRole(RaftRole.FOLLOWER);
        }

        if (request.getTerm() == node.getCurrentTerm()) {
            node.setRole(RaftRole.FOLLOWER);
            resetElectionTimeout();

            if (isLogMatch(request.getPrevLogIndex(), request.getPrevLogTerm())) {
                appendEntries(request.getEntries());
                if (request.getLeaderCommit() > node.getCommitIndex()) {
                    node.setCommitIndex(Math.min(request.getLeaderCommit(), node.getLastLogIndex()));
                }
                response.setSuccess(true);
            } else {
                response.setSuccess(false);
            }
        } else {
            response.setSuccess(false);
        }
    }

    private boolean isLogUpToDate(long lastLogTerm, int lastLogIndex) {
        // 判断日志是否是最新的
        return true;
    }

    private boolean isLogMatch(int prevLogIndex, long prevLogTerm) {
        // 判断日志是否匹配
        return true;
    }

    private void appendEntries(List<LogEntry> entries) {
        // 追加日志条目
    }

    private void resetElectionTimeout() {
        // 重置选举超时时间
    }
}