package com.muyang.mq.server.dao;

import com.muyang.mq.MqApplication;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/14
 * Time: 22:04
 */
@SpringBootTest
@Slf4j
class MemoryManagerTest {
    MemoryManager memoryManager;

    @BeforeEach
    void setUp() {
        memoryManager = new MemoryManager();
    }

    @AfterEach
    void tearDown() {
        memoryManager = null;
    }

    // 创建一个测试交换机
    private Exchange createTestExchange(String exchangeName) {
        Exchange exchange = new Exchange();
        exchange.setName(exchangeName);
        exchange.setType(ExchangeType.DIRECT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        return exchange;
    }

    // 创建一个测试队列
    private QueueCore createTestQueue(String queueName) {
        QueueCore queue = new QueueCore();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setExclusive(false);
        queue.setAutoDelete(false);
        return queue;
    }

    // 针对交换机进行测试
    @Test
    public void testExchange() throws MqException {
        // 1. 先构造一个交换机并插入.
        Exchange expectedExchange = createTestExchange("testExchange");
        memoryManager.insertExchange(expectedExchange);
        // 2. 查询出这个交换机, 比较结果是否一致. 此处直接比较这俩引用指向同一个对象.
        Exchange actualExchange = memoryManager.getExchange("testExchange");
        Assertions.assertEquals(expectedExchange, actualExchange);
        // 3. 删除这个交换机
        memoryManager.deleteExchange("testExchange");
        // 4. 再次查询队列, 看是否能查到
        actualExchange = memoryManager.getExchange("testExchange");
        assertNull(actualExchange);
//        try {
//            actualExchange = memoryManager.getExchange("testExchange");
//        } catch (MqException e) {
//            log.info(e.toString());
//        }
    }

    // 针对队列进行测试
    @Test
    public void testQueue() throws MqException {
        // 1. 构造一个队列, 并插入
        QueueCore expectedQueue = createTestQueue("testQueue");
        memoryManager.insertQueue(expectedQueue);
        // 2. 查询这个队列, 并比较
        QueueCore actualQueue = memoryManager.getQueue("testQueue");
        Assertions.assertEquals(expectedQueue, actualQueue);
        // 3. 删除这个队列
        memoryManager.deleteQueue("testQueue");
        // 4. 再次查询队列, 看是否能查到
        actualQueue = memoryManager.getQueue("testQueue");
        Assertions.assertNull(actualQueue);
//
//        try {
//            actualQueue = memoryManager.getQueue("testQueue");
//        } catch (MqException e) {
//            log.info(e.toString());
//        }
    }

    // 针对绑定进行测试
    @Test
    public void testBinding() throws MqException {
        Binding expectedBinding = new Binding();
        expectedBinding.setExchangeName("testExchange");
        expectedBinding.setQueueName("testQueue");
        expectedBinding.setBindingKey("testBindingKey");
        memoryManager.insertBinding(expectedBinding);

        Binding actualBinding = memoryManager.getBinding("testExchange", "testQueue");
        Assertions.assertEquals(expectedBinding, actualBinding);

        ConcurrentHashMap<String, Binding> bindingMap = memoryManager.getBindings("testExchange");
        Assertions.assertEquals(1, bindingMap.size());
        Assertions.assertEquals(expectedBinding, bindingMap.get("testQueue"));

        memoryManager.deleteBinding(expectedBinding);

        actualBinding = memoryManager.getBinding("testExchange", "testQueue");
        Assertions.assertNull(actualBinding);
    }

    private Msg createTestMessage(String content) {
        Msg message = Msg.createMsgWithId("testRoutingKey", null, content.getBytes());
        return message;
    }

    @Test
    public void testMessage() {
        Msg expectedMessage = createTestMessage("testMessage");
        memoryManager.insertMsg(expectedMessage);

        Msg actualMessage = memoryManager.getMessage(expectedMessage.getBasicProperties().getId());
        Assertions.assertEquals(expectedMessage, actualMessage);

        memoryManager.deleteMessage(expectedMessage.getBasicProperties().getId());

        actualMessage = memoryManager.getMessage(expectedMessage.getBasicProperties().getId());
        Assertions.assertNull(actualMessage);
    }

    @Test
    public void testSendMessage() throws MqException {
        // 1. 创建一个队列, 创建 10 条消息, 把这些消息都插入队列中.
        QueueCore queue = createTestQueue("testQueue");
        List<Msg> expectedMessages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Msg message = createTestMessage("testMessage" + i);
            memoryManager.sendMsg(queue, message);
            expectedMessages.add(message);
        }

        // 2. 从队列中取出这些消息.
        List<Msg> actualMessages = new ArrayList<>();
        while (true) {
            Msg message = memoryManager.pollMsg("testQueue");
            if (message == null) {
                break;
            }
            actualMessages.add(message);
        }

        // 3. 比较取出的消息和之前的消息是否一致.
        Assertions.assertEquals(expectedMessages.size(), actualMessages.size());
        for (int i = 0; i < expectedMessages.size(); i++) {
            Assertions.assertEquals(expectedMessages.get(i), actualMessages.get(i));
        }
    }

    @Test
    public void testMessageWaitAck() throws MqException {
        Msg expectedMessage = createTestMessage("expectedMessage");
        memoryManager.addMsgWaitAck("testQueue", expectedMessage);

        Msg actualMessage = memoryManager.getMessageWaitAck("testQueue", expectedMessage.getBasicProperties().getId());
        Assertions.assertEquals(expectedMessage, actualMessage);

        memoryManager.removeMessageWaitAck("testQueue", expectedMessage.getBasicProperties().getId());
        actualMessage = memoryManager.getMessageWaitAck("testQueue", expectedMessage.getBasicProperties().getId());
        Assertions.assertNull(actualMessage);
    }

    @Test
    public void testRecovery() throws IOException, MqException, ClassNotFoundException {
        // 由于后续需要进行数据库操作, 依赖 MyBatis. 就需要先启动 SpringApplication, 这样才能进行后续的数据库操作.
        MqApplication.context = SpringApplication.run(MqApplication.class);

        // 1. 在硬盘上构造好数据

        DiskDataManager diskDataManager = new DiskDataManager();

        // 构造交换机
        Exchange expectedExchange = createTestExchange("testExchange");
        diskDataManager.insertExchange(expectedExchange);

        // 构造队列
        QueueCore expectedQueue = createTestQueue("testQueue");
        diskDataManager.insertQueue(expectedQueue);

        // 构造绑定
        Binding expectedBinding = new Binding();
        expectedBinding.setExchangeName("testExchange");
        expectedBinding.setQueueName("testQueue");
        expectedBinding.setBindingKey("testBindingKey");
        diskDataManager.insertBinding(expectedBinding);

        // 构造消息
        Msg expectedMessage = createTestMessage("testContent");
        diskDataManager.insertMsg(expectedQueue, expectedMessage);

        log.info("================");
        log.info("执行恢复操作中...");
        // 2. 执行恢复操作
        memoryManager.recovery(diskDataManager);

        // 3. 对比结果
        Exchange actualExchange = memoryManager.getExchange("testExchange");
        Assertions.assertEquals(expectedExchange.getName(), actualExchange.getName());
        Assertions.assertEquals(expectedExchange.getType(), actualExchange.getType());
        Assertions.assertEquals(expectedExchange.getDurable(), actualExchange.getDurable());
        Assertions.assertEquals(expectedExchange.getAutoDelete(), actualExchange.getAutoDelete());

        QueueCore actualQueue = memoryManager.getQueue("testQueue");
        Assertions.assertEquals(expectedQueue.getName(), actualQueue.getName());
        Assertions.assertEquals(expectedQueue.getDurable(), actualQueue.getDurable());
        Assertions.assertEquals(expectedQueue.getAutoDelete(), actualQueue.getAutoDelete());
        Assertions.assertEquals(expectedQueue.getExclusive(), actualQueue.getExclusive());

        Binding actualBinding = memoryManager.getBinding("testExchange", "testQueue");
        Assertions.assertEquals(expectedBinding.getExchangeName(), actualBinding.getExchangeName());
        Assertions.assertEquals(expectedBinding.getQueueName(), actualBinding.getQueueName());
        Assertions.assertEquals(expectedBinding.getBindingKey(), actualBinding.getBindingKey());

        Msg actualMessage = memoryManager.pollMsg("testQueue");
        Assertions.assertEquals(expectedMessage.getBasicProperties().getId(), actualMessage.getBasicProperties().getId());
        Assertions.assertEquals(expectedMessage.getBasicProperties().getRoutingKey(), actualMessage.getBasicProperties().getRoutingKey());
        Assertions.assertEquals(expectedMessage.getBasicProperties().getDeliverMode(), actualMessage.getBasicProperties().getDeliverMode());
        Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());

        // 4. 清理硬盘的数据, 把整个 data 目录里的内容都删掉(包含了 meta.db 和 队列的目录).
        MqApplication.context.close();
        File dataDir = new File("./data");
        FileUtils.deleteDirectory(dataDir);
    }
}