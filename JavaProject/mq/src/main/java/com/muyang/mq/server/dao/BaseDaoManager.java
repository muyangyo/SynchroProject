package com.muyang.mq.server.dao;

import com.muyang.mq.MqApplication;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.QueueCore;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import com.muyang.mq.server.mapper.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/3
 * Time: 21:40
 */
//@Repository
@Slf4j
public class BaseDaoManager {
    //    @Autowired
    private BaseMapper baseMapper;

    public void init() {
        //为了可以删除文件,只能手动从启动类引入对象
        this.baseMapper = MqApplication.context.getBean(BaseMapper.class);

        // 先判定数据库文件存在不存在
        if (!checkDBExists()) {
            log.info("数据库初始化中...");
            // 数据库不存在,先创建一个 data 目录(避免找不到路径)
            File dataDir = new File("./data");
            dataDir.mkdirs();
            // 创建数据表 (数据库文件会自动生成)
            createTable();
            // 插入默认数据
            createDefaultData();
            log.info("数据库初始化完成!");
        } else {
            // 数据库已经存在了, 啥都不必做即可
            log.info("数据库已经存在!");
        }
    }

    public void deleteDB() {
        File file = new File("./data/meta.db");
        if (file.delete()) {
            log.info("db删除成功!");
        } else {
            log.warn("db文件删除失败!");
        }

        File folder = new File("./data");
        if (folder.delete()) {
            log.info("文件夹删除成功!");
        } else {
            log.warn("文件夹删除失败!");
        }
    }

    private void createDefaultData() {
        Exchange exchange = new Exchange();
        exchange.setName("");//默认生成一个 匿名交换机
        exchange.setType(ExchangeType.DIRECT);
        exchange.setAutoDelete(false);
        exchange.setDurable(true);
        baseMapper.insertExchange(exchange);
    }

    private void createTable() {
        baseMapper.createExchangeTable();
        baseMapper.createBindingTable();
        baseMapper.createQueueTable();
    }

    private boolean checkDBExists() {
        File file = new File("./data/meta.db");
        return file.exists();
    }


    /*
     * 包装mapper
     * */
    public void insertExchange(Exchange exchange) {
        baseMapper.insertExchange(exchange);
    }

    public List<Exchange> selectAllExchanges() {
        return baseMapper.selectAllExchanges();
    }

    public void deleteExchange(String exchangeName) {
        baseMapper.deleteExchange(exchangeName);
    }

    public void insertQueue(QueueCore queue) {
        baseMapper.insertQueue(queue);
    }

    public List<QueueCore> selectAllQueues() {
        return baseMapper.selectAllQueues();
    }

    public void deleteQueue(String queueName) {
        baseMapper.deleteQueue(queueName);
    }

    public void insertBinding(Binding binding) {
        baseMapper.insertBinding(binding);
    }

    public List<Binding> selectAllBindings() {
        return baseMapper.selectAllBindings();
    }

    public void deleteBinding(Binding binding) {
        baseMapper.deleteBinding(binding);
    }
}
