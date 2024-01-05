package com.muyang.mq.server.dao;

import com.muyang.mq.MqApplication;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/3
 * Time: 22:06
 */
@SpringBootTest
class BaseDaoManagerTest {
    //    @Autowired
    private BaseDaoManager daoManager = new BaseDaoManager();

    @BeforeEach
    void setUp() {
        MqApplication.context = SpringApplication.run(MqApplication.class);
        daoManager.init();
    }

    @AfterEach
    void tearDown() {
        /*
         * 这里必须先关闭服务, 才能删除数据库, 否则会删除失败(Spring服务会持有数据库⽂件的访问权限)
         * 当然,Linux不会,Linux使用的是内存上的临时文件
         * */
        MqApplication.context.close();
        daoManager.deleteDB();
    }

    @Test
    void createTables() {
        // 获取全部的数据
        List<Exchange> exchangeList = daoManager.selectAllExchanges();
        List<QueueCore> queueList = daoManager.selectAllQueues();
        List<Binding> bindingList = daoManager.selectAllBindings();
        // 验证交换机的数据正确性
        Assertions.assertEquals(1, exchangeList.size());
        Assertions.assertEquals("", exchangeList.get(0).getName());
        Assertions.assertEquals(ExchangeType.DIRECT, exchangeList.get(0).getType());
        // 验证其他表的数据正确性
        Assertions.assertEquals(0, queueList.size());
        Assertions.assertEquals(0, bindingList.size());
    }

    @Test
    void exchangeInsertAndDelete() {
        //插入交换机
        Exchange exchange = new Exchange();
        exchange.setName("testExchange");
        exchange.setType(ExchangeType.FANOUT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        exchange.putArgsMap("aaa", 1);
        exchange.putArgsMap("bbb", 2);

        daoManager.insertExchange(exchange);
        List<Exchange> exchangeList = daoManager.selectAllExchanges();
        //验证是否正确
        Exchange newExchange = exchangeList.get(1);
        Assertions.assertEquals("testExchange", newExchange.getName());
        Assertions.assertEquals(ExchangeType.FANOUT, newExchange.getType());
        Assertions.assertEquals(false, newExchange.getAutoDelete());
        Assertions.assertEquals(true, newExchange.getDurable());
        Assertions.assertEquals(1, newExchange.getArgsMap("aaa"));
        Assertions.assertEquals(2, newExchange.getArgsMap("bbb"));

        // 进行删除操作
        daoManager.deleteExchange("testExchange");
        // 再次验证
        exchangeList = daoManager.selectAllExchanges();
        Assertions.assertEquals(1, exchangeList.size());
        Assertions.assertEquals("", exchangeList.get(0).getName());
    }

    @Test
    void queueInsertAndDelete() {
        QueueCore queue = new QueueCore();
        queue.setName("testQueue");
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        queue.putArgsMap("aaa", 1);
        queue.putArgsMap("bbb", 2);

        daoManager.insertQueue(queue);
        List<QueueCore> queueList = daoManager.selectAllQueues();

        Assertions.assertEquals(1, queueList.size());
        QueueCore newQueue = queueList.get(0);
        Assertions.assertEquals("testQueue", newQueue.getName());
        Assertions.assertEquals(true, newQueue.getDurable());
        Assertions.assertEquals(false, newQueue.getAutoDelete());
        Assertions.assertEquals(false, newQueue.getExclusive());
        Assertions.assertEquals(1, newQueue.getArgsMap("aaa"));
        Assertions.assertEquals(2, newQueue.getArgsMap("bbb"));

        daoManager.deleteQueue("testQueue");
        queueList = daoManager.selectAllQueues();
        Assertions.assertEquals(0, queueList.size());
    }

    @Test
    void bindingInsertAndDelete() {
        Binding binding = new Binding();
        binding.setExchangeName("testExchange");
        binding.setQueueName("testQueue");
        binding.setBindingKey("testBindingKey");

        daoManager.insertBinding(binding);

        List<Binding> bindingList = daoManager.selectAllBindings();
        Assertions.assertEquals(1, bindingList.size());
        Assertions.assertEquals("testExchange", bindingList.get(0).getExchangeName());
        Assertions.assertEquals("testQueue", bindingList.get(0).getQueueName());
        Assertions.assertEquals("testBindingKey", bindingList.get(0).getBindingKey());

        daoManager.deleteBinding(binding);
        bindingList = daoManager.selectAllBindings();
        Assertions.assertEquals(0, bindingList.size());
    }
}