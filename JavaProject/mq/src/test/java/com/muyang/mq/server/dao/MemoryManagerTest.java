package com.muyang.mq.server.dao;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
        // 4. 再查一次, 看是否就查不到了
        try {
            actualExchange = memoryManager.getExchange("testExchange");
        } catch (MqException e) {
            log.info(e.toString());
        }
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
        try {
            actualQueue = memoryManager.getQueue("testQueue");
        } catch (MqException e) {
            log.info(e.toString());
        }
    }
}