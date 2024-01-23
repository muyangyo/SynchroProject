package com.muyang.mq.server.brokercore;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/23
 * Time: 17:29
 */

import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.common.ConsumerEnv;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.VirtualHost;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
public class ConsumerManager {
    // 持有上层的 VirtualHost 对象的引用,用来操作数据
    private VirtualHost virtualHostParent;
    // 指定一个线程池, 负责去执行具体的回调任务(不管扫描)
    private ExecutorService workPlace = Executors.newFixedThreadPool(4);
    // 存放令牌的队列(不限制大小)
    private BlockingQueue<String> tokenQueue = new LinkedBlockingQueue<>();
    // 扫描线程
    private Thread scannerThread = null;

    public ConsumerManager(VirtualHost virtualHost) {
        this.virtualHostParent = virtualHost;

        this.scannerThread = new Thread(() -> {
            while (true) {
                try {
                    // 1. 拿到令牌
                    String queueName = tokenQueue.take();
                    // 2. 根据令牌, 找到队列
                    QueueCore queue = virtualHostParent.getMemoryManager().getQueue(queueName);
                    if (queue == null) {
                        throw new MqException("没有 " + queueName + " 队列,无法消费消息!");
                    }
                    // 3. 从这个队列中消费一个消息.
                    synchronized (queue) {
                        consumeMessage(queue);//可能其他线程也在消费消息
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });
        // 把线程设为后台线程.
        this.scannerThread.setDaemon(true);
        this.scannerThread.start();
    }

    /**
     * 调用时机:发送消息的时候
     *
     * @param queueName 信息发送到的队列
     */
    public void notifyConsume(String queueName) throws InterruptedException {
        tokenQueue.put(queueName);//告诉阻塞队列这个队列有新消息了
    }


    public void addConsumer(String consumerTag, String queueName, boolean autoAck, ConsumerBehavior consumerBehavior) throws
            MqException {
        // 找到对应的队列.
        QueueCore queue = virtualHostParent.getMemoryManager().getQueue(queueName);
        if (queue == null) {
            throw new MqException(queueName + " 队列不存在!");
        }
        ConsumerEnv consumerEnv = new ConsumerEnv(consumerTag, queueName, autoAck, consumerBehavior);
        synchronized (queue) {
            queue.addConsumerEnv(consumerEnv);
            // 如果当前队列中已经有了一些消息了, 需要立即就消费掉.
            int n = virtualHostParent.getMemoryManager().getMsgCount(queueName);
            for (int i = 0; i < n; i++) {
                // 这个方法调用一次就消费一条消息.
                consumeMessage(queue);
            }
        }

    }

    private void consumeMessage(QueueCore queue) {

    }
}
