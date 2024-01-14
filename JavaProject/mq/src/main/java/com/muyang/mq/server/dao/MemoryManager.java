package com.muyang.mq.server.dao;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Exchange;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.QueueCore;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/14
 * Time: 10:57
 */
@Slf4j
public class MemoryManager {
    //用来存储 交换机 的, key 是 exchangeName ,value 是 Exchange 对象
    private ConcurrentHashMap<String, Exchange> exchangeMap = new ConcurrentHashMap<>();
    //用来存储 队列 的, key 是 queueName ,value 是 queue 对象
    private ConcurrentHashMap<String, QueueCore> queueMap = new ConcurrentHashMap<>();
    //用来存储 绑定  的, key 是 exchangeName ,value 是该交换机的队列绑定表.内部的 key 是 queueName, value 是 binding 对象
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Binding>> bindingsMap = new ConcurrentHashMap<>();
    //用来存储全部的 信息 的, key 是 MsgId ,value 是 Msg 对象
    private ConcurrentHashMap<String, Msg> msgMap = new ConcurrentHashMap<>();

    //队列关联信息使用,key 是 queueName,value 是 Msg 的链表(属于这个队列的全部信息)
    private ConcurrentHashMap<String, LinkedList<Msg>> queueMsgMap = new ConcurrentHashMap<>();
    //存储待应答消息,key是 queueName,value 是待应答的 Msg 的 map.内部的 key 是 msgId , value 是 msg 对象
    private ConcurrentHashMap<String, ConcurrentHashMap<String, Msg>> waitAckMap = new ConcurrentHashMap<>();

    public void insertExchange(Exchange exchange) {
        exchangeMap.put(exchange.getName(), exchange);
        log.info("成功添加交换机:" + exchange.getName());
    }

    public Exchange getExchange(String exchangeName) throws MqException {
        Exchange exchange = exchangeMap.get(exchangeName);
        if (exchange == null) {
            throw new MqException("无此交换机!");
        }
        return exchange;
    }

    public void deleteExchange(String exchangeName) {
        exchangeMap.remove(exchangeName);
        log.info("成功删除交换机:" + exchangeName);
    }

    public void insertQueue(QueueCore queueCore) {
        queueMap.put(queueCore.getName(), queueCore);
        log.info("成功添加队列:" + queueCore.getName());
    }

    public QueueCore getQueue(String queueName) throws MqException {
        QueueCore queueCore = queueMap.get(queueName);
        if (queueCore == null) {
            throw new MqException("无此队列!");
        }
        return queueCore;
    }

    public void deleteQueue(String queueName) {
        queueMap.remove(queueName);
        log.info("成功删除队列:" + queueName);
    }

    public void insertBinding(Binding binding) {
        //单个交换机的 binding
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
        if (bindingMap == null) {
            //先判断一次,避免频繁加锁
            synchronized (bindingsMap) {
                if (bindingMap == null) {
                    //如果当前这个交换机没有绑定表,则创建一个
                    ConcurrentHashMap<String, Binding> tempMap = new ConcurrentHashMap<>();
                    bindingsMap.put(binding.getExchangeName(), tempMap);
                    //然后再插入
                    tempMap.put(binding.getQueueName(), binding);
                    log.info("绑定添加成功!(锁情况1添加)");
                } else {
                    //解决第一个线程已经在外层if内了,而第二个线程刚刚把绑定表创建完成的情况
                    bindingMap.put(binding.getQueueName(), binding);
                    log.info("绑定添加成功!(锁情况2添加)");
                }
            }

        } else {
            //存在直接加
            bindingMap.put(binding.getQueueName(), binding);
            log.info("绑定添加成功!(非锁情况添加)");
        }
    }

    //返回一个binding
    public Binding getBinding(String exchangeName, String queueName) {
        return (bindingsMap.get(exchangeName)).get(queueName);
    }

    //获取该交换机的绑定表
    public ConcurrentHashMap<String, Binding> getBindings(String exchangeName) {
        return bindingsMap.get(exchangeName);
    }

    public void deleteBinding(Binding binding) throws MqException {
        ConcurrentHashMap<String, Binding> bindingMap = bindingsMap.get(binding.getExchangeName());
        if (bindingMap == null) {
            //不存在这个绑定表.(避免空指针异常,这个不存在不是删空的,是没有插入该绑定过)
            throw new MqException("不存在该绑定表" + binding.getExchangeName() + " 与 " + binding.getQueueName());
        }
        bindingMap.remove(binding.getQueueName());
        log.info("成功删除绑定!" + binding.getExchangeName() + " 与 " + binding.getQueueName());
    }


    //存入 信息总表
    public void insertMsg(Msg message) {
        msgMap.put(message.getBasicProperties().getId(), message);
        log.info("id为: {} 的信息已存入总表!", message.getBasicProperties().getId());
    }

    //从 信息总表 根据 id 获取信息
    public Msg getMessage(String messageId) {
        return msgMap.get(messageId);
    }

    //从 信息总表 根据 id 删除信息
    public void removeMessage(String messageId) {
        msgMap.remove(messageId);
        log.info("id为: {} 的消息已移出总表!", messageId);
    }


    // 发送消息到指定队列
    public void sendMsg(QueueCore queue, Msg message) {
        LinkedList<Msg> list = queueMsgMap.computeIfAbsent(queue.getName(), k -> new LinkedList<>());
        //已经避免了 空指针 问题
        synchronized (list) {
            list.add(message);//避免重复写问题(或者其他线程冲突问题)
        }
        log.info("id为: {} 的消息已经发送到指定队列", message.getBasicProperties().getId());
        insertMsg(message);//写入消息总表,这里不担心重复写的问题.反正对于hashMap而言,重复写也一样

    }

    // 从队列中取消息
    public Msg pollMsg(String queueName) throws MqException {
        LinkedList<Msg> list = queueMsgMap.get(queueName);
        if (list == null) {
            throw new MqException(queueName + " 队列没有创建消息链表!请先进行初次发送消息到指定的队列");
        }
        synchronized (list) {
            if (list.size() == 0) {
                return null;//没有信息的时候
            }
            Msg msg = list.remove(0);
            log.info("{} 信息已从 {} 取出", msg.getBasicProperties().getId(), queueName);
            return msg;
        }
    }

    // 获取指定队列中消息的个数
    public int getMsgCount(String queueName) throws MqException {
        LinkedList<Msg> list = queueMsgMap.get(queueName);
        if (list == null) {
            log.warn(queueName + " 队列没有创建消息链表!请先进行初次发送消息到指定的队列,再获取消息个数!");
            return 0;
        }
        synchronized (list) {
            return list.size();
        }
    }

    // 添加未确认的消息到待应答消息表
    public void addMsgWaitAck(String queueName, Msg msg) {
        ConcurrentHashMap<String, Msg> curQueueWaitAckMap = waitAckMap.computeIfAbsent(queueName, k -> new ConcurrentHashMap<>());
        curQueueWaitAckMap.put(msg.getBasicProperties().getId(), msg);
        log.info("id为 {} 的消息已经加入到待应答的表中", msg.getBasicProperties().getId());
    }

    // 删除未确认的消息(消息已经确认了就可以删除了)
    public void removeMessageWaitAck(String queueName, String msgId) throws MqException {
        ConcurrentHashMap<String, Msg> curQueueWaitAckMap = waitAckMap.get(queueName);
        if (curQueueWaitAckMap == null) {
            throw new MqException("要删除的消息所属的队列不存在或没有进行初次信息发送");
        }
        curQueueWaitAckMap.remove(msgId);
        log.info("id为 {} 的消息已应答完成", msgId);
    }

    // 获取指定的未确认的消息
    public Msg getMessageWaitAck(String queueName, String msgId) {
        ConcurrentHashMap<String, Msg> curQueueWaitAckMap = waitAckMap.get(queueName);
        if (curQueueWaitAckMap == null) {
            log.warn("要获取的消息所属的队列不存在或没有进行初次信息发送");
            return null;
        }
        return curQueueWaitAckMap.get(msgId);
    }

    // 这个方法就是从硬盘上读取数据, 把硬盘中之前持久化存储的各个维度的数据都恢复到内存中.
    public void recovery(DiskDataManager diskDataManager) throws IOException, MqException, ClassNotFoundException {
        // 1. 清空之前的所有数据
        exchangeMap.clear();
        queueMap.clear();
        bindingsMap.clear();
        msgMap.clear();
        queueMsgMap.clear();
        waitAckMap.clear();
        // 2. 恢复所有的交换机数据
        List<Exchange> exchanges = diskDataManager.selectAllExchange();
        for (Exchange temp : exchanges) {
            exchangeMap.put(temp.getName(), temp);
        }
        // 3. 恢复所有的队列数据
        List<QueueCore> queueCores = diskDataManager.selectAllQueue();
        for (QueueCore temp : queueCores) {
            queueMap.put(temp.getName(), temp);
        }
        // 4. 恢复所有的绑定数据
        List<Binding> bindings = diskDataManager.selectAllBinding();
        for (Binding temp : bindings) {
            ConcurrentHashMap<String, Binding> tempMap = bindingsMap.computeIfAbsent(temp.getExchangeName(), k -> new ConcurrentHashMap<>());
            tempMap.put(temp.getQueueName(), temp);
        }
        // 5. 恢复所有的消息数据
        // 要恢复所有的数据,必须对 队列 进行遍历
        for (QueueCore temp : queueCores) {
            LinkedList<Msg> list = diskDataManager.selectAllMsg(temp.getName());
            queueMsgMap.put(temp.getName(), list);//先放入进队列关联的信息链表中

            for (Msg msgTemp : list) {
                msgMap.put(msgTemp.getBasicProperties().getId(), msgTemp);//再放入到 信息中心
            }
        }
        //这里不恢复 "未应答消息",在等待 ack 的过程中, 如果服务器重启了, 此时这些 "未应答消息", 就恢复成 "未被取走的消息".
    }
}
