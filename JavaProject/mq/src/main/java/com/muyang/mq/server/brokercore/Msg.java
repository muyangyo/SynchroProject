package com.muyang.mq.server.brokercore;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 13:54
 */
@Data
public class Msg implements Serializable {
    private BasicProperties basicProperties;//属性部分
    private Byte[] body;//正文

    //持久化存储 附加属性 [)
    private transient Long offsetBegin = 0L; //消息数据的开头 距离 文件开头文字的偏移量(字节)
    private transient Long offsetEnd = 0L;
    private Byte isValid = 0x01; //该条消息在 文件里 是否有效,0x00无效,0x01有效


    public static Msg createMsgWithId(String routingKey, BasicProperties basicProperties, Byte[] body) {
        Msg msg = new Msg();

        if (basicProperties != null) {
            msg.setBasicProperties(basicProperties);
        } else {
            //避免空指针问题
            msg.setBasicProperties(new BasicProperties());
        }
        //生成唯一的ID (这里使用UUID进行生成)
        msg.getBasicProperties().setId("Msg-" + UUID.randomUUID().toString());
        msg.getBasicProperties().setRoutingKey(routingKey);

        msg.setBody(body);
        return msg;
    }

    public static Msg createMsgWithId(BasicProperties basicProperties, Byte[] body) {
        Msg msg = new Msg();

        if (basicProperties != null) {
            msg.setBasicProperties(basicProperties);
        } else {
            //避免空指针问题
            msg.setBasicProperties(new BasicProperties());
        }
        //生成唯一的ID (这里使用UUID进行生成)
        msg.getBasicProperties().setId("Msg-" + UUID.randomUUID());

        msg.setBody(body);
        return msg;
    }
}
