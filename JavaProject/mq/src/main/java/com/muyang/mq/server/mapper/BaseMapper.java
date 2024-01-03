package com.muyang.mq.server.mapper;

import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.QueueCore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/3
 * Time: 11:50
 */
@Mapper
public interface BaseMapper {
    // 建表方法(这里没有 消息 建表,是因为消息的操作比较频繁,数据库吃不消,消息直接使用文件进行存储)
    Integer createExchangeTable();

    Integer createQueueTable();

    Integer createBindingTable();


    // 三张表的基础操作

    Integer insertExchange(Exchange exchange);

    List<Exchange> selectAllExchanges();

    Integer deleteExchange(String exchangeName);


    Integer insertQueue(QueueCore queueCore);

    List<QueueCore> selectAllQueues();

    Integer deleteQueue(String queueName);

    Integer insertBinding(Binding binding);

    List<Binding> selectAllBindings();

    Integer deleteBinding(Binding binding);

}
