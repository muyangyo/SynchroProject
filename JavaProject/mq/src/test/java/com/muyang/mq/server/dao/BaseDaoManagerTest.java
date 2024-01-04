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
    void selectAllExchanges() {
    }

    @Test
    void deleteExchange() {
    }

    @Test
    void insertQueue() {
    }

    @Test
    void selectAllQueues() {
    }

    @Test
    void deleteQueue() {
    }

    @Test
    void insertBinding() {
    }

    @Test
    void selectAllBindings() {
    }

    @Test
    void deleteBinding() {
    }
}