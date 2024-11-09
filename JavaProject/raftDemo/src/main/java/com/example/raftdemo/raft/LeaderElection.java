import com.example.raftdemo.config.RaftConfig;
import com.example.raftdemo.config.RaftNode;
import com.example.raftdemo.config.RaftRole;

import java.util.Random;

public class LeaderElection {

    private RaftNode node;
    private Random random;

    public LeaderElection(RaftNode node) {
        this.node = node;
        this.random = new Random();
        resetElectionTimeout();
    }

    public void startElection() {
        node.setRole(RaftRole.CANDIDATE);
        node.setCurrentTerm(node.getCurrentTerm() + 1);
        node.setVotedFor(node.getId());
        resetElectionTimeout();

        // 向其他节点发送RequestVote请求
        for (RaftNode peer : getPeers()) {
            sendRequestVote(peer);
        }
    }

    public void resetElectionTimeout() {
        node.setElectionTimeout(RaftConfig.ELECTION_TIMEOUT_MIN + random.nextInt(RaftConfig.ELECTION_TIMEOUT_MAX - RaftConfig.ELECTION_TIMEOUT_MIN));
        node.setLastHeartbeatTime(System.currentTimeMillis());
    }

    public void checkElectionTimeout() {
        if (node.getRole() == RaftRole.FOLLOWER && System.currentTimeMillis() - node.getLastHeartbeatTime() > node.getElectionTimeout()) {
            startElection();
        }
    }

    private void sendRequestVote(RaftNode peer) {
        // 发送RequestVote请求到peer节点
    }
}