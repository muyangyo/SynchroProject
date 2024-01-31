package com.muyang.mq.server.dao;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;

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

/**
 * 这个类用来包装
 * <p> Msg(本地文件) 消息 </p>
 * <p> SQL(SQLite) 交换机, 绑定, 队列 </p>
 */
public class DiskDataManager {
    private final MsgFileManager msgFileManager;    //提供信息操作对象
    private final SQLDaoManager sqlDaoManager;      //提供sql操作对象

    /**
     * DiskDataManager的构造函数，初始化msgFileManager和sqlDaoManager
     */
    public DiskDataManager() {
        this.msgFileManager = new MsgFileManager();
        msgFileManager.init();//目前为空方法,为了形式统一
        this.sqlDaoManager = new SQLDaoManager();
        sqlDaoManager.init();
    }


    /**
     * 将交换机信息插入数据库
     *
     * @param exchange 要插入的交换机
     */
    public void insertExchange(Exchange exchange) {
        sqlDaoManager.insertExchange(exchange);
    }

    /**
     * 根据交换机名称删除数据库中的信息
     *
     * @param exchangeName 交换机名称
     */
    public void deleteExchange(String exchangeName) {
        sqlDaoManager.deleteExchange(exchangeName);
    }


    /**
     * 查询数据库中所有交换机信息并返回
     *
     * @return 所有交换机信息
     */
    public List<Exchange> selectAllExchange() {
        return sqlDaoManager.selectAllExchanges();
    }


    /**
     * 将队列信息插入数据库,并且创建队列的相应文件
     *
     * @param queueCore 要插入的队列
     */
    public void insertQueue(QueueCore queueCore) throws IOException {
        sqlDaoManager.insertQueue(queueCore);
        // 创建队列的同时, 不仅仅是把队列对象写到数据库中, 还需要创建出对应的目录和文件
        msgFileManager.createQueueFiles(queueCore.getName());
    }

    /**
     * 根据队列名称从数据库中删除信息,同时删除队列的相应文件
     *
     * @param queueName 要删除的队列名
     */
    public void deleteQueue(String queueName) throws IOException {
        sqlDaoManager.deleteQueue(queueName);
        // 删除队列的同时, 不仅仅是把队列从数据库中删除, 还需要删除对应的目录和文件
        msgFileManager.destroyQueueFiles(queueName);
    }


    /**
     * 查询数据库中所有队列信息并返回
     *
     * @return 所有队列信息
     */
    public List<QueueCore> selectAllQueue() {
        return sqlDaoManager.selectAllQueues();
    }


    /**
     * 将绑定信息插入数据库
     *
     * @param binding 要插入的绑定对象
     */
    public void insertBinding(Binding binding) {
        sqlDaoManager.insertBinding(binding);
    }

    /**
     * 根据绑定从数据库中删除信息
     *
     * @param binding 要删除的绑定对象
     */
    public void deleteBinding(Binding binding) {
        sqlDaoManager.deleteBinding(binding);
    }

    /**
     * 查询数据库中所有绑定信息并返回
     *
     * @return 所有绑定信息
     */
    public List<Binding> selectAllBinding() {
        return sqlDaoManager.selectAllBindings();
    }


    /**
     * (磁盘操作)将信息插入到指定队列的 相应文件 上 (这里为了统一形式,全部采用类似数据库的方法名命名)
     *
     * @param queue   信息所属队列
     * @param message 信息
     */
    public void insertMsg(QueueCore queue, Msg message) throws IOException, MqException {
        msgFileManager.sendMessage(queue, message);
    }

    /**
     * (磁盘操作)将信息从指定队列的 相应文件 上删除 (这里为了统一形式,全部采用类似数据库的方法名命名)
     *
     * @param queue   信息所属队列
     * @param message 信息
     */
    public void deleteMsg(QueueCore queue, Msg message) throws IOException, ClassNotFoundException, MqException {
        msgFileManager.deleteMessage(queue, message);
        //检查是否触发 GC
        if (msgFileManager.checkGC(queue.getName())) {
            msgFileManager.gc(queue);
        }
    }

    /**
     * (磁盘操作)将 所有属于该队列的信息 从指定队列的 相应文件 上读取出来 (这里为了统一形式,全部采用类似数据库的方法名命名)
     *
     * @param queueName 队列名
     * @return 所有属于该队列的信息
     */
    public LinkedList<Msg> selectAllMsg(String queueName) throws IOException, MqException, ClassNotFoundException {
        return msgFileManager.loadAllMessageFromQueue(queueName);
    }

}
