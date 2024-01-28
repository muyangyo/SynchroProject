package com.muyang.mq.client;

import com.muyang.mq.common.BinTool;
import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.common.MqException;
import com.muyang.mq.common.network.Request;
import com.muyang.mq.common.network.request_args.*;
import com.muyang.mq.common.network.response_args.BasicResponseArguments;
import com.muyang.mq.server.brokercore.BasicProperties;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/28
 * Time: 20:59
 */
//一个基本的通信单元
@Data
@Slf4j
public class Channel {
    private String channelId;
    // 当前这个 channel 属于哪个连接
    private Connection connection;
    // 用来存储后续客户端收到的服务器的响应(邮箱),由于有connection分发线程和主线程同时使用,所以需要用线程安全的map
    private ConcurrentHashMap<String, BasicResponseArguments> mail = new ConcurrentHashMap<>();
    // 一个 Channel 中只能有一个回调函数(处理逻辑)
    private ConsumerBehavior behavior = null; //订阅后设置
    //处理 用户行为 的池
    private ExecutorService callBackPool;


    public Channel(String channelId, Connection connection) {
        this.channelId = channelId;
        this.connection = connection;
        callBackPool = Executors.newFixedThreadPool(1);
    }

    /**
     * 告知服务器 客户端已经创建了一个频道
     */
    public boolean createChannel() throws IOException {
        //发送一个创建请求

        //1. 构造载荷
        BasicRequestArguments requestArguments = new BasicRequestArguments();
        requestArguments.setChannelId(channelId);
        requestArguments.setRid(generateRid());
        byte[] payload = BinTool.toBytes(requestArguments);

        //2.构造请求
        Request request = new Request();
        request.setType(0x1);
        request.setLength(payload.length);
        request.setPayload(payload);

        //3.发送请求
        connection.writeRequest(request);
        //4.等待服务器的响应
        BasicResponseArguments responseArguments = waitResult(requestArguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 等待服务器返回消息到 Connection 后,分发到这个频道
     */
    private BasicResponseArguments waitResult(String rid) {
        BasicResponseArguments responseArguments = null;
        while ((responseArguments = mail.get(rid)) == null) {
            //无法获取 对应 rid 的消息,则说明消息还没回来,阻塞主线程
            synchronized (this) {
                try {
                    log.info("当前线程正在等待回应!请求id为: {}", rid);
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        // 读取成功之后,删除消息表中的消息
        mail.remove(rid);
        return responseArguments;
    }

    /**
     * 唤醒主线程和发布消息(由Connection类进行操作)
     */
    public void putReturns(BasicResponseArguments responseArguments) {
        mail.put(responseArguments.getRid(), responseArguments);
        synchronized (this) {
            notifyAll();//唤醒主线程
        }
    }

    /**
     * 生成一个rid(内部调用)
     */
    private String generateRid() {
        return "R-" + UUID.randomUUID().toString();
    }

    /**
     * 关闭 channel, 给服务器发送一个 type = 0x2 的请求
     */
    public boolean close() throws IOException {
        //1.构造请求载荷
        BasicRequestArguments basicArguments = new BasicRequestArguments();
        basicArguments.setRid(generateRid());
        basicArguments.setChannelId(channelId);
        byte[] payload = BinTool.toBytes(basicArguments);

        //2.构造请求
        Request request = new Request();
        request.setType(0x2);
        request.setLength(payload.length);
        request.setPayload(payload);

        //3.借用connection发送请求
        connection.writeRequest(request);
        //4.阻塞等待响应
        BasicResponseArguments responseArguments = waitResult(basicArguments.getRid());
        //5.销毁channel
        this.callBackPool.shutdownNow();
        this.mail.clear();
        return responseArguments.isOk();
    }

    /**
     * 创建交换机
     */
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        ExchangeDeclareRequestArguments exchangeDeclareArguments = new ExchangeDeclareRequestArguments();
        exchangeDeclareArguments.setRid(generateRid());
        exchangeDeclareArguments.setChannelId(channelId);
        exchangeDeclareArguments.setExchangeName(exchangeName);
        exchangeDeclareArguments.setExchangeType(exchangeType);
        exchangeDeclareArguments.setDurable(durable);
        exchangeDeclareArguments.setAutoDelete(autoDelete);
        exchangeDeclareArguments.setArguments(arguments);
        byte[] payload = BinTool.toBytes(exchangeDeclareArguments);

        Request request = new Request();
        request.setType(0x3);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(exchangeDeclareArguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 删除交换机
     */
    public boolean exchangeDelete(String exchangeName) throws IOException {
        ExchangeDeleteRequestArguments arguments = new ExchangeDeleteRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setExchangeName(exchangeName);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x4);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 创建队列
     */
    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) throws IOException {
        QueueDeclareRequestArguments queueDeclareArguments = new QueueDeclareRequestArguments();
        queueDeclareArguments.setRid(generateRid());
        queueDeclareArguments.setChannelId(channelId);
        queueDeclareArguments.setQueueName(queueName);
        queueDeclareArguments.setDurable(durable);
        queueDeclareArguments.setExclusive(exclusive);
        queueDeclareArguments.setAutoDelete(autoDelete);
        queueDeclareArguments.setArguments(arguments);
        byte[] payload = BinTool.toBytes(queueDeclareArguments);

        Request request = new Request();
        request.setType(0x5);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(queueDeclareArguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 删除队列
     */
    public boolean queueDelete(String queueName) throws IOException {
        QueueDeleteRequestArguments arguments = new QueueDeleteRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x6);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 创建绑定
     */
    public boolean queueBind(String queueName, String exchangeName, String bindingKey) throws IOException {
        QueueBindRequestArguments arguments = new QueueBindRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setExchangeName(exchangeName);
        arguments.setBindingKey(bindingKey);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x7);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 解除绑定
     */
    public boolean queueUnbind(String queueName, String exchangeName) throws IOException {
        QueueUnbindRequestArguments arguments = new QueueUnbindRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setExchangeName(exchangeName);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x8);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 发送消息
     */

    public boolean basicPublish(String exchangeName, String routingKey, BasicProperties basicProperties, byte[] body) throws IOException {
        BasicPublishRequestArguments arguments = new BasicPublishRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setExchangeName(exchangeName);
        arguments.setRoutingKey(routingKey);
        arguments.setBasicProperties(basicProperties);
        arguments.setBody(body);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0x9);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 订阅消息
     */
    public boolean basicConsume(String queueName, boolean autoAck, ConsumerBehavior consumer) throws MqException, IOException {
        // 先设置回调.
        if (this.behavior != null) {
            log.warn("无法重复设置回调函数!");
            return false;
        }
        this.behavior = consumer;

        BasicConsumeRequestArguments arguments = new BasicConsumeRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setConsumerTag(channelId);  // 此处 consumerTag 也使用 channelId 来表示了.
        arguments.setQueueName(queueName);
        arguments.setAutoAck(autoAck);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0xa);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

    /**
     * 确认消息
     */
    public boolean basicAck(String queueName, String messageId) throws IOException {
        BasicAckRequestArguments arguments = new BasicAckRequestArguments();
        arguments.setRid(generateRid());
        arguments.setChannelId(channelId);
        arguments.setQueueName(queueName);
        arguments.setMessageId(messageId);
        byte[] payload = BinTool.toBytes(arguments);

        Request request = new Request();
        request.setType(0xb);
        request.setLength(payload.length);
        request.setPayload(payload);

        connection.writeRequest(request);
        BasicResponseArguments responseArguments = waitResult(arguments.getRid());
        return responseArguments.isOk();
    }

}
