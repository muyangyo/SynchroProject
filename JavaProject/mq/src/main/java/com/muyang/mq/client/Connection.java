package com.muyang.mq.client;

import com.muyang.mq.common.BinTool;
import com.muyang.mq.common.MqException;
import com.muyang.mq.common.network.Request;
import com.muyang.mq.common.network.Response;
import com.muyang.mq.common.network.response_args.BasicResponseArguments;
import com.muyang.mq.common.network.response_args.MsgResponseArguments;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 负责创建channel,分发响应,提供写请求和读取响应
 */
@Slf4j

public class Connection {
    private Socket socket = null;
    // 管理channel的,key 是 channelId,value 是 channel 对象
    private ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();


    //socket所持有的流对象
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;


    //建立一个连接
    public Connection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        dataInputStream = new DataInputStream(inputStream);
        dataOutputStream = new DataOutputStream(outputStream);


        // 创建一个扫描线程, 由这个线程负责不停的从 socket 中读取响应数据. 把这个响应数据再交给对应的 channel 负责处理(只负责分发,不负责处理)
        Thread t = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    Response response = readResponse();
                    dispatchResponse(response);//分发响应给channel
                }
            } catch (SocketException e) {
                //这里是正常结束
                log.info("[客户端] 连接正常断开!");
            } catch (IOException | ClassNotFoundException | MqException e) {
                //非正常结束
                e.printStackTrace();
                log.error("[客户端] 连接异常断开!");
            }
        });
        t.start();
    }

    // 分发响应,看看这个响应是返回的操作指令还是消息
    private void dispatchResponse(Response response) throws IOException, ClassNotFoundException, MqException {
        if (response.getType() == 0xc) {
            // 这个响应是消息数据
            MsgResponseArguments msgResponseArguments = (MsgResponseArguments) BinTool.fromBytes(response.getPayload());
            // 根据 channelId 找到对应的 channel 对象
            Channel channel = channelMap.get(msgResponseArguments.getChannelId());
            if (channel == null) {
                throw new MqException("[客户端] 该消息对应的 channel 在客户端中不存在! channelId=" + channel.getChannelId());
            }
            // 执行该 channel 对象内部的回调函数
            channel.getCallBackPool().submit(() -> {
                try {
                    channel.getBehavior().handleDelivery(msgResponseArguments.getConsumerTag(), msgResponseArguments.getBasicProperties(),
                            msgResponseArguments.getBody());
                } catch (MqException | IOException e) {
                    e.printStackTrace();
                }
            });
        } else {
            // 当前响应是针对刚才的控制请求的响应
            BasicResponseArguments basicResponseArguments = (BasicResponseArguments) BinTool.fromBytes(response.getPayload());
            // 把这个结果放到对应的 channel 的 hash 表中(交给它内部自己处理)
            Channel channel = channelMap.get(basicResponseArguments.getChannelId());
            if (channel == null) {
                throw new MqException("[客户端] 该消息对应的 channel 在客户端中不存在! channelId=" + channel.getChannelId());
            }
            channel.putReturns(basicResponseArguments);//唤醒主线程
        }
    }

    /**
     * 关闭连接使用
     */
    public void close() {
        try {
            channelMap.clear();
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送请求
     */
    public void writeRequest(Request request) throws IOException {
        dataOutputStream.writeInt(request.getType());
        dataOutputStream.writeInt(request.getLength());
        dataOutputStream.write(request.getPayload());
        dataOutputStream.flush();
        log.info("[客户端] 发送请求成功! type=" + request.getType() + ", length=" + request.getLength());
    }

    /**
     * 读取响应
     */
    public Response readResponse() throws IOException {
        Response response = new Response();
        response.setType(dataInputStream.readInt());
        response.setLength(dataInputStream.readInt());
        byte[] payload = new byte[response.getLength()];
        int n = dataInputStream.read(payload);
        if (n != response.getLength()) {
            throw new IOException("读取的响应数据不完整!");
        }
        response.setPayload(payload);
        log.info("[客户端] 收到响应! type=" + response.getType() + ", length=" + response.getLength());
        return response;
    }

    // 通过这个方法, 在 Connection 中能够创建出一个 Channel
    public Channel createChannel() throws IOException {
        String channelId = "C-" + UUID.randomUUID().toString();
        Channel channel = new Channel(channelId, this);
        channelMap.put(channelId, channel);
        // 把 创建 channel 的这个消息告诉服务器,让服务器知道有这个频道存在了
        boolean ok = channel.createChannel();
        if (!ok) {
            //服务器无法留存信息,创建失败,删除记录
            channelMap.remove(channelId);
            return null;
        }
        return channel;
    }
}
