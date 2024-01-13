package com.muyang.mq.server.dao;

import com.muyang.mq.MqApplication;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/13
 * Time: 22:20
 */
@SpringBootTest
class DiskDataManagerTest {
    DiskDataManager diskDataManager;
    MsgFileManager msgFileManager;
    private static final String queueName1 = "testQueue1";

    @BeforeEach
    void setUp() throws IOException {
        MqApplication.context = SpringApplication.run(MqApplication.class);
        diskDataManager = new DiskDataManager();
        msgFileManager = new MsgFileManager();
        //每次测试前创建下文件
        msgFileManager.createQueueFiles(queueName1);
    }

    @AfterEach
    void tearDown() throws IOException {
        MqApplication.context.close();
        //每次测试后删除文件
        msgFileManager.destroyQueueFiles(queueName1);
    }


    private QueueCore createTestQueue(String queueName) {
        QueueCore queue = new QueueCore();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        return queue;
    }

    private Msg createTestMsg(String body) {
        return Msg.createMsgWithId("testDemo", null, body.getBytes());
    }

    @Test
    void Msg() throws IOException, MqException, ClassNotFoundException {
        QueueCore queue = createTestQueue(queueName1);
        Msg msg1 = createTestMsg("123");
        diskDataManager.insertMsg(queue, msg1);
        Msg msg2 = createTestMsg("1234");
        diskDataManager.insertMsg(queue, msg2);

        LinkedList<Msg> preList = diskDataManager.selectAllMsg(queue.getName());
        Assertions.assertEquals(2, preList.size());

        diskDataManager.deleteMsg(queue, msg2);
        LinkedList<Msg> afterList = diskDataManager.selectAllMsg(queue.getName());
        Assertions.assertEquals(1, afterList.size());

    }


}