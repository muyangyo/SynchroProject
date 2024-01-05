package com.muyang.mq.common;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/5
 * Time: 17:04
 */
// 将 对象 进行序列化和反序列化的
public class BinTool {
     // 把一个对象序列化成一个字节数组
    public static byte[] toBytes(Object object) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            // 此处的 writeObject 就会把该对象进行序列化, 生成的二进制字节数据, 写入到 ByteArrayOutputStream 里了
            objectOutputStream.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        }
    }

    // 把一个字节数组, 反序列化成一个对象
    public static Object fromBytes(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream)) {
            return objectInputStream.readObject();
        }
    }

}
