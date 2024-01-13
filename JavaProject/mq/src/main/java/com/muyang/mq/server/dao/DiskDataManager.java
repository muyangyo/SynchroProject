package com.muyang.mq.server.dao;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/13
 * Time: 21:57
 */
/*
 * 这个类用来包装
 * Msg(本地文件):消息
 * SQL(SQLite):交换机, 绑定, 队列
 * 统一操作
 * */

public class DiskDataManager {
    private final MsgFileManager msgFileManager;
    private final SQLDaoManager sqlDaoManager;

    public DiskDataManager() {
        this.msgFileManager = new MsgFileManager();
        msgFileManager.init();//目前为空方法,为了形式统一
        this.sqlDaoManager = new SQLDaoManager();
        sqlDaoManager.init();
    }

    //包装交换机的方法
    public void insertExchange(Exchange exchange) {
        sqlDaoManager.insertExchange(exchange);
    }

    public void deleteExchange(String exchangeName) {
        sqlDaoManager.deleteExchange(exchangeName);
    }

    public List<Exchange> selectAllExchange() {
        return sqlDaoManager.selectAllExchanges();
    }


    //包装队列的方法
    public void insertQueue(QueueCore queueCore) {
        sqlDaoManager.insertQueue(queueCore);
    }

    public void deleteQueue(String queueName) {
        sqlDaoManager.deleteQueue(queueName);
    }

    public List<QueueCore> selectAllQueue() {
        return sqlDaoManager.selectAllQueues();
    }


    //包装绑定
    public void insertBinding(Binding binding) {
        sqlDaoManager.insertBinding(binding);
    }

    public void deleteBinding(Binding binding) {
        sqlDaoManager.deleteBinding(binding);
    }

    public List<Binding> selectAllBinding() {
        return sqlDaoManager.selectAllBindings();
    }

    //包装信息 (这里为了统一形式,全部采用类似数据库的方法名命名)
    public void insertMsg(QueueCore queue, Msg message) throws IOException, MqException {
        msgFileManager.sendMessage(queue, message);
    }

    public void deleteMsg(QueueCore queue, Msg message) throws IOException, ClassNotFoundException, MqException {
        msgFileManager.deleteMessage(queue, message);
        //检查是否触发 GC
        if (msgFileManager.checkGC(queue.getName())) {
            msgFileManager.gc(queue);
        }
    }

    public LinkedList<Msg> selectAllMsg(String queueName) throws IOException, MqException, ClassNotFoundException {
        return msgFileManager.loadAllMessageFromQueue(queueName);
    }

}
