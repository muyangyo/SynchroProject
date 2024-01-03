package com.muyang.mq.server.brokercore;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/2
 * Time: 13:52
 */
@Data
public class QueueCore {
    private String name;//队列名称
    private Boolean durable = false;//是否持久化
    private Boolean exclusive = false;//是否为专属(对消费者而言)
    private Boolean autoDelete = false;//是否自动删除
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Map<String, Object> args = new HashMap<>();//额外参数

//    public String getArgs() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        String ret = "";
//        try {
//            ret = objectMapper.writeValueAsString(args);
//            return ret;
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void setArgs(String string) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            this.args = objectMapper.readValue(string, new TypeReference<HashMap<String, Object>>() {
//            }); //有泛型的类型,必须要用 TypeReference 捕获和操作泛型类型信息
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
