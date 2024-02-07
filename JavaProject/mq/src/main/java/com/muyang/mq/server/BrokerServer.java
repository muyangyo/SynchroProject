package com.muyang.mq.server;

import com.muyang.mq.common.BinTool;
import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.common.MqException;
import com.muyang.mq.common.network.Request;
import com.muyang.mq.common.network.Response;
import com.muyang.mq.common.network.request_args.*;
import com.muyang.mq.common.network.response_args.BasicResponseArguments;
import com.muyang.mq.common.network.response_args.MsgResponseArguments;
import com.muyang.mq.server.brokercore.BasicProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/28
 * Time: 17:04
 */
@Slf4j
public class BrokerServer {
    private ServerSocket serverSocket = null;

    // 当前只考虑一个 BrokerServer 上只有一个 虚拟主机(后期可以该map)
    private VirtualHost virtualHost = new VirtualHost("default-");
    // 使用这个 哈希表 表示当前的所有会话(也就是说有哪些客户端正在和咱们的服务器进行通信)
    //  key 是 channelId, value 为对应的 Socket 对象(对socket对象进行复用)
    private ConcurrentHashMap<String, Socket> sessions = new ConcurrentHashMap<String, Socket>();
    // 引入一个线程池, 来接受多个客户端的请求
    private ExecutorService executorService = null;
    // 引入一个 boolean 变量控制服务器是否需要继续运行
    private volatile boolean runnable = true;

    public BrokerServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void stop() throws IOException {
        runnable = false;
        executorService.shutdown();
        serverSocket.close();
    }

    public void start() throws IOException {
        log.info("MQ服务器已启动!");
        executorService = Executors.newCachedThreadPool();//弄一个可变的线程池来处理请求
        try {
            while (runnable) {
                Socket clientSocket = serverSocket.accept();
                // 把处理连接的逻辑丢给这个线程池,主线程继续等其他连接
                executorService.submit(() -> {
                    processConnection(clientSocket);
                });
            }
        } catch (SocketException e) {
            log.error("MQ服务器被强制关闭!");
        } finally {
            log.info("MQ服务器停止运行!");
        }
    }

    // 通过这个方法来处理每次 tcp 连接,在这一个连接中, 可能会涉及到多个请求和响应.
    private void processConnection(Socket clientSocket) {
        try (InputStream inputStream = clientSocket.getInputStream(); OutputStream outputStream = clientSocket.getOutputStream()) {
            try (DataInputStream dataInputStream = new DataInputStream(inputStream); DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
                while (true) {
                    // 1. 读取请求
                    Request request = readRequest(dataInputStream);
                    // 2. 根据请求计算响应
                    Response response = process(request, clientSocket);
                    // 3. 返回响应
                    writeResponse(dataOutputStream, response);
                }
            }
        } catch (EOFException | SocketException e) {
            // 这里会因为 TCP连接断开 导致读取到 EOF 或者 SocketException
            log.info("TCP 连接已关闭! 客户端的地址: " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());
        } catch (IOException | ClassNotFoundException | MqException e) {
            e.printStackTrace();
            log.error("TCP 连接出现异常!");
        } finally {
            try {
                // 关闭 socket
                clientSocket.close();
                // 一个 TCP 连接中, 可能包含多个 channel. 需要把当前这个 socket 对应的所有 channel 也顺便清理掉
                clearClosedSession(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //读取请求,将 二进制数据 转换为 Request 对象
    private Request readRequest(DataInputStream dataInputStream) throws IOException {
        Request request = new Request();
        request.setType(dataInputStream.readInt());
        request.setLength(dataInputStream.readInt());
        byte[] payload = new byte[request.getLength()];
        int n = dataInputStream.read(payload);//n为实际读取的数据
        if (n != request.getLength()) {
            throw new IOException("读取请求格式出错!");
        }
        request.setPayload(payload);
        return request;
    }

    //写响应,将 Response 对象 转换为 二进制数据
    private void writeResponse(DataOutputStream dataOutputStream, Response response) throws IOException {
        dataOutputStream.writeInt(response.getType());
        dataOutputStream.writeInt(response.getLength());
        dataOutputStream.write(response.getPayload());
        dataOutputStream.flush();
    }


    //处理请求
    private Response process(Request request, Socket clientSocket) throws IOException, ClassNotFoundException, MqException {
        // 1. 把 request 中的 payload 做一个初步的解析.
        BasicRequestArguments requestArgs = (BasicRequestArguments) BinTool.fromBytes(request.getPayload());
        log.info("请求读取成功!请求的类型为: {} ,请求的载荷长度为: {} ,请求的Id为: {} ,频道为: {} ", request.getType(), request.getLength(), requestArgs.getRid(), requestArgs.getChannelId());
        // 2. 根据 type 的值, 来进一步区分接下来这次请求要干啥,进而将载荷转化为更具体的对象,以读取其参数
        boolean ok = true;
        if (request.getType() == 0x01) {
            // 创建 channel
            sessions.put(requestArgs.getChannelId(), clientSocket);
            log.info("创建 channel 完成! 频道ID为: {}", requestArgs.getChannelId());
        } else if (request.getType() == 0x02) {
            // 销毁 channel
            sessions.remove(requestArgs.getChannelId());
            log.info("销毁 channel 完成! 频道Id为: {}", requestArgs.getChannelId());
        } else if (request.getType() == 0x03) {
            // 创建交换机
            ExchangeDeclareRequestArguments arguments = (ExchangeDeclareRequestArguments) requestArgs;
            ok = virtualHost.exchangeDeclare(arguments.getExchangeName(), arguments.getExchangeType(), arguments.isDurable(), arguments.isAutoDelete(), arguments.getArguments());
        } else if (request.getType() == 0x04) {
            ExchangeDeleteRequestArguments arguments = (ExchangeDeleteRequestArguments) requestArgs;
            ok = virtualHost.exchangeDelete(arguments.getExchangeName());
        } else if (request.getType() == 0x05) {
            QueueDeclareRequestArguments arguments = (QueueDeclareRequestArguments) requestArgs;
            ok = virtualHost.queueDeclare(arguments.getQueueName(), arguments.isDurable(), arguments.isExclusive(), arguments.isAutoDelete(), arguments.getArguments());
        } else if (request.getType() == 0x06) {
            QueueDeleteRequestArguments arguments = (QueueDeleteRequestArguments) requestArgs;
            ok = virtualHost.queueDelete((arguments.getQueueName()));
        } else if (request.getType() == 0x07) {
            QueueBindRequestArguments arguments = (QueueBindRequestArguments) requestArgs;
            ok = virtualHost.queueBind(arguments.getQueueName(), arguments.getExchangeName(), arguments.getBindingKey());
        } else if (request.getType() == 0x08) {
            QueueUnbindRequestArguments arguments = (QueueUnbindRequestArguments) requestArgs;
            ok = virtualHost.queueUnbind(arguments.getQueueName(), arguments.getExchangeName());
        } else if (request.getType() == 0x09) {
            BasicPublishRequestArguments arguments = (BasicPublishRequestArguments) requestArgs;
            ok = virtualHost.basicPublish(arguments.getExchangeName(), arguments.getRoutingKey(), arguments.getBasicProperties(), arguments.getBody());
        } else if (request.getType() == 0x0a) {
            BasicConsumeRequestArguments arguments = (BasicConsumeRequestArguments) requestArgs;
            ok = virtualHost.basicConsume(arguments.getConsumerTag(), arguments.getQueueName(), arguments.isAutoAck(), new ConsumerBehavior() {
                // 这个回调函数要做的工作(当有消息时才会触发), 就是把服务器收到的消息 直接推送 回对应的消费者客户端(让客户端自己再跑自定义的行为即可)
                @Override
                public void handleDelivery(String channelId, BasicProperties basicProperties, byte[] body) throws MqException, IOException {
                    // 先知道当前这个收到的消息, 要发给哪个客户端.
                    // 1. 根据 channelId 找到 socket 对象
                    Socket clientSocket = sessions.get(channelId);
                    if (clientSocket == null || clientSocket.isClosed()) {
                        throw new MqException("订阅消息的客户端已经关闭!");
                    }
                    // 2. 构造响应数据
                    MsgResponseArguments msgResponseArguments = new MsgResponseArguments();
                    msgResponseArguments.setChannelId(channelId);
                    msgResponseArguments.setRid(""); // 由于这里只有响应, 没有请求, 不需要去对应,所以就不需要rid
                    msgResponseArguments.setOk(true);

                    msgResponseArguments.setConsumerTag(channelId);
                    msgResponseArguments.setBasicProperties(basicProperties);
                    msgResponseArguments.setBody(body);

                    byte[] payload = BinTool.toBytes(msgResponseArguments);
                    Response response = new Response();
                    response.setType(0x0c);
                    response.setLength(payload.length);
                    response.setPayload(payload);
                    // 3. 把数据写回给客户端.
                    /**
                     *  注意! 此处的 dataOutputStream 这个对象不能 close !!!
                     *  如果 把 dataOutputStream 关闭, 就会把 clientSocket 里的 outputStream 也关了,
                     *  导致无法再往这个 socket 里写数据了
                     */
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                    writeResponse(dataOutputStream, response);
                }
            });
        } else if (request.getType() == 0x0b) {
            // 调用 basicAck 确认消息
            BasicAckRequestArguments arguments = (BasicAckRequestArguments) requestArgs;
            ok = virtualHost.basicAck(arguments.getQueueName(), arguments.getMessageId());
        } else {
            // 当前的 type 是非法的
            throw new MqException("未定义的 type! type=" + request.getType());
        }
        // 3. 构造响应(前面都是处理了请求而已)
        BasicResponseArguments basicReturns = new BasicResponseArguments();
        basicReturns.setChannelId(requestArgs.getChannelId());
        basicReturns.setRid(requestArgs.getRid());
        basicReturns.setOk(ok);
        byte[] payload = BinTool.toBytes(basicReturns);
        Response response = new Response();
        response.setType(request.getType());
        response.setLength(payload.length);
        response.setPayload(payload);
        log.info("已处理完一次请求:{},返回响应的为:{}", requestArgs.getRid(), response);
        return response;
    }

    private void clearClosedSession(Socket clientSocket) {
        // 这里要做的事情就是遍历上述 sessions 表, 把该被关闭的 socket 有关的 channel 都给删掉
        List<String> toDeleteChannelId = new ArrayList<>();
        for (Map.Entry<String, Socket> entry : sessions.entrySet()) {
            if (entry.getValue() == clientSocket) {
                /**
                 * 这里不能直接删除正在遍历的集合,要不然会导致迭代器出现问题
                 */
                toDeleteChannelId.add(entry.getKey());
            }
        }
        //先记录,再删除
        for (String channelId : toDeleteChannelId) {
            sessions.remove(channelId);
        }
        log.info("成功清除所有使用TCP连接IP为: {}:{} 的channel", clientSocket.getInetAddress(), clientSocket.getPort());
    }

}
