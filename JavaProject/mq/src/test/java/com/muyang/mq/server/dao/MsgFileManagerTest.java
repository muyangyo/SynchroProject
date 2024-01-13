package com.muyang.mq.server.dao;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.dao.model.Stats;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/13
 * Time: 12:16
 */
@SpringBootTest
@Slf4j
class MsgFileManagerTest {

    MsgFileManager msgFileManager = new MsgFileManager();

    private static final String queueName1 = "testQueue1";
    private static final String queueName2 = "testQueue2";

    @BeforeEach
    void setUp() throws IOException {
        //每次测试前创建下文件
        msgFileManager.createQueueFiles(queueName1);
        msgFileManager.createQueueFiles(queueName2);
    }

    @AfterEach
    void tearDown() throws IOException {
        //每次测试后删除文件
        msgFileManager.destroyQueueFiles(queueName1);
        msgFileManager.destroyQueueFiles(queueName2);
    }

    @Test
    public void testCreateFiles() {
        File data1 = new File("./data/" + queueName1 + "/queue_data.txt");
        Assertions.assertEquals(true, data1.isFile());
        File stats1 = new File("./data/" + queueName1 + "/queue_stats.txt");
        Assertions.assertEquals(true, stats1.isFile());

        File data2 = new File("./data/" + queueName2 + "/queue_data.txt");
        Assertions.assertEquals(true, data2.isFile());
        File stats2 = new File("./data/" + queueName2 + "/queue_stats.txt");
        assertTrue(stats2.isFile());
    }

    @Test
    public void testReadWriteStat() {
        Stats stats = new Stats();
        stats.setValidCount(0);
        stats.setTotalCount(0);
        // 此处就需要使用反射的方式, 来调用 writeStats 和 readStats
        // 此处使用 Spring 帮我们封装好的 反射 测试工具类.Java原生的有点难用
        ReflectionTestUtils.invokeMethod(msgFileManager, "writeStats", queueName1, stats);

        Stats stats1 = ReflectionTestUtils.invokeMethod(msgFileManager, "readStats", queueName1);
        assert stats1 != null;
        Assertions.assertEquals(stats1.getValidCount(), 0);
        Assertions.assertEquals(stats1.getTotalCount(), 0);
    }


    private Msg createTestMsg(String body) {
        Msg msg = Msg.createMsgWithId("testDemo", null, body.getBytes());
        return msg;
    }

    private QueueCore createTestQueue(String queueName) {
        QueueCore queue = new QueueCore();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        return queue;
    }

    @Test
    void sendMessage() throws IOException, MqException, ClassNotFoundException {
        Msg preMsg = createTestMsg("testMsg");
        QueueCore preQueue = createTestQueue(queueName1);

        msgFileManager.sendMessage(preQueue, preMsg);

        //检查文件是否成功写入
        //先检查统计文件
        Stats stats = ReflectionTestUtils.invokeMethod(msgFileManager, "readStats", queueName1);
        assert stats != null;
        Assertions.assertEquals(1, stats.getTotalCount());
        Assertions.assertEquals(1, stats.getValidCount());

        //检查数据文件
        LinkedList<Msg> list = msgFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(1, list.size());
        Msg msg = list.get(0);
        //注意比较数组不能使用 assertEquals
        Assertions.assertArrayEquals(msg.getBody(), preMsg.getBody());
        Assertions.assertEquals(msg.getBasicProperties().getId(), preMsg.getBasicProperties().getId());
        Assertions.assertEquals(msg.getBasicProperties().getRoutingKey(), preMsg.getBasicProperties().getRoutingKey());
    }

    @Test
    public void loadAllMessageFromQueue() throws IOException, MqException, ClassNotFoundException {
        // 往队列中插入 100 条消息
        QueueCore queue = createTestQueue(queueName1);
        List<Msg> expectedMessages = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Msg message = createTestMsg("testMessage" + i);
            msgFileManager.sendMessage(queue, message);
            expectedMessages.add(message);
        }

        // 读取所有消息
        LinkedList<Msg> actualMessages = msgFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(expectedMessages.size(), actualMessages.size());
        for (int i = 0; i < expectedMessages.size(); i++) {
            Msg expectedMessage = expectedMessages.get(i);
            Msg actualMessage = actualMessages.get(i);
            log.info("[" + i + "] actualMessage=" + actualMessage);

            Assertions.assertEquals(expectedMessage.getBasicProperties().getId(), actualMessage.getBasicProperties().getId());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getRoutingKey(), actualMessage.getBasicProperties().getRoutingKey());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getDeliverMode(), actualMessage.getBasicProperties().getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());
            Assertions.assertEquals(0x1, actualMessage.getIsValid());
        }
    }


    @Test
    void DeleteMessage() throws IOException, MqException, ClassNotFoundException {
        QueueCore queue = createTestQueue(queueName1);
        List<Msg> expectedMessages = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Msg message = createTestMsg("testMessage" + i);
            msgFileManager.sendMessage(queue, message);
            expectedMessages.add(message);
        }

        // 删除其中的三个消息
        msgFileManager.deleteMessage(queue, expectedMessages.get(7));
        msgFileManager.deleteMessage(queue, expectedMessages.get(8));
        msgFileManager.deleteMessage(queue, expectedMessages.get(9));

        LinkedList<Msg> actualMessages = msgFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(7, actualMessages.size());
        for (int i = 0; i < actualMessages.size(); i++) {
            Msg expectedMessage = expectedMessages.get(i);
            Msg actualMessage = actualMessages.get(i);
            log.info("[" + i + "] actualMessage=" + actualMessage);

            Assertions.assertEquals(expectedMessage.getBasicProperties().getId(), actualMessage.getBasicProperties().getId());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getRoutingKey(), actualMessage.getBasicProperties().getRoutingKey());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getDeliverMode(), actualMessage.getBasicProperties().getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());
            Assertions.assertEquals(0x1, actualMessage.getIsValid());
        }
    }


    @Test
    void gc() throws IOException, MqException, ClassNotFoundException {
        // 先往队列中写 100 个消息. 获取到文件大小. 再把 100 个消息中的一半, 都给删除掉(比如把下标为偶数的消息都删除)
        QueueCore queue = createTestQueue(queueName1);
        List<Msg> expectedMessages = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Msg message = createTestMsg("testMessage" + i);
            msgFileManager.sendMessage(queue, message);
            expectedMessages.add(message);
        }

        // 获取 gc 前的文件大小
        File beforeGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long beforeGCLength = beforeGCFile.length();
        log.info("GC之前的文件大小为: {}", beforeGCLength);

        // 删除偶数下标的消息
        for (int i = 0; i < 100; i += 2) {
            msgFileManager.deleteMessage(queue, expectedMessages.get(i));
        }

        // 手动调用 gc (默认是2000条,1000无效触发,我们手动触发下)
        long start = System.currentTimeMillis();
        msgFileManager.gc(queue);
        long end = System.currentTimeMillis();
        log.info("GC大约耗时为: {} ms", end - start);

        // 重新读取文件, 验证新的文件的内容是不是和之前的内容匹配
        LinkedList<Msg> actualMessages = msgFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(50, actualMessages.size());
        for (int i = 0; i < actualMessages.size(); i++) {

            Msg expectedMessage = expectedMessages.get(2 * i + 1);
            Msg actualMessage = actualMessages.get(i);

            Assertions.assertEquals(expectedMessage.getBasicProperties().getId(), actualMessage.getBasicProperties().getId());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getRoutingKey(), actualMessage.getBasicProperties().getRoutingKey());
            Assertions.assertEquals(expectedMessage.getBasicProperties().getDeliverMode(), actualMessage.getBasicProperties().getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());
            Assertions.assertEquals(0x1, actualMessage.getIsValid());
        }

        // 获取新的文件的大小
        File afterGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long afterGCLength = afterGCFile.length();
        log.info("GC之后的文件大小为: {}", afterGCLength);

        Assertions.assertTrue(beforeGCLength > afterGCLength);
    }
}