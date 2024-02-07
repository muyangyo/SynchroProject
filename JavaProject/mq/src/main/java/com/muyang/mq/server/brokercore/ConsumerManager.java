package com.muyang.mq.server.brokercore;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/23
 * Time: 17:29
 */

import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.common.Consumer;
import com.muyang.mq.common.MqException;
import com.muyang.mq.server.VirtualHost;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 */
@Slf4j
public class ConsumerManager {
    // 持有上层的 VirtualHost 对象的引用,用来操作数据
    private VirtualHost virtualHostParent;
    // 指定一个线程池, 负责去执行具体的回调任务(不管扫描)
    private ExecutorService workPlace = Executors.newFixedThreadPool(4);
    // 新信息通知 阻塞队列(可感知 所有 的队列的新信息)
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


    public void addConsumer(String consumerTag, String queueName, boolean autoAck, ConsumerBehavior consumerBehavior) throws MqException {
        // 找到对应的队列.
        QueueCore queue = virtualHostParent.getMemoryManager().getQueue(queueName);
        if (queue == null) {
            throw new MqException(queueName + " 队列不存在!");
        }
        Consumer consumer = new Consumer(consumerTag, queueName, autoAck, consumerBehavior);
        synchronized (queue) {
            queue.addConsumerEnv(consumer);
            // 如果当前队列中已经有了一些消息了, 需要立即就消费掉.
            int n = virtualHostParent.getMemoryManager().getMsgCount(queueName);
            for (int i = 0; i < n; i++) {
                // 这个方法调用一次就消费一条消息.
                consumeMessage(queue);
            }
        }

    }

    private void consumeMessage(QueueCore queue) {
        // 1. 按照轮询的方式, 找个消费者出来.
        Consumer consumer = queue.chooseConsumer();
        if (consumer == null) {
            log.warn("当前队列{}没有消费者,无法消费!", queue.getName());
            return;
        }
        try {
            // 2. 从队列中取出一个消息
            Msg msg = virtualHostParent.getMemoryManager().pollMsg(queue.getName());
            if (msg == null) {
                log.warn("目前队列{}还没有消息,无法进行消费!", queue.getName());
                return;
            }

            // 3. 把消息带入到消费者的回调方法中, 丢给线程池执行.
            workPlace.submit(() -> {

                try {
                    // 1. 把消息放到待确认的集合中. 这个操作势必在执行回调之前.
                    virtualHostParent.getMemoryManager().addMsgWaitAck(queue.getName(), msg);
                    // 2. 真正执行回调操作
                    consumer.getConsumerBehavior().handleDelivery(consumer.getConsumerTag(), msg
                            .getBasicProperties(), msg.getBody());
                    // 3. 如果当前是 "自动应答" , 就可以直接把消息删除了.
                    //如果当前是 "手动应答" , 则先不处理, 交给后续消费者调用 basicAck 方法来处理.
                    if (consumer.isAutoAck()) {
                        // 1) 删除硬盘上的消息
                        if (msg.getBasicProperties().getDeliverMode() == 2) {
                            virtualHostParent.getDiskDataManager().deleteMsg(queue, msg);
                        }
                        // 2) 删除上面的待 确认表 中的消息
                        virtualHostParent.getMemoryManager().removeMessageWaitAck(queue.getName(), msg.getBasicProperties().getId());
                        // 3) 删除内存中 消息总表 里的消息
                        virtualHostParent.getMemoryManager().deleteMessage(msg.getBasicProperties().getId());
                        log.info("消息:{} 被成功消费! 队列名为:{}", msg.getBasicProperties().getId(), queue.getName());
                    }
                } catch (MqException | IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            });


        } catch (MqException e) {
            throw new RuntimeException(e);
        }

    }
}
