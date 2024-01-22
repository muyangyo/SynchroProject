package com.muyang.mq.server.brokercore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

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
    //    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Map<String, Object> args = new HashMap<>();//额外参数

    /**
     * 给数据库用的,将 args 转为字符串
     *
     * @return Map字符串, 给数据库用的
     */
    public String getArgs() {
        ObjectMapper objectMapper = new ObjectMapper();
        String ret = "";
        try {
            ret = objectMapper.writeValueAsString(args);
            return ret;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从数据库中读取字符串转换为Map
     *
     * @param string 数据库提供
     */
    public void setArgs(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.args = objectMapper.readValue(string, new TypeReference<HashMap<String, Object>>() {
            }); //有泛型的类型,必须要用 TypeReference 捕获和操作泛型类型信息
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接设置Map
     *
     * @param arguments 其他参数表
     */
    public void setArgs(Map<String, Object> arguments) {
        this.args = arguments;
    }

    /*
     * 下面这两个是为了方便设置 hashMap 的 KV
     * */
    public void putArgsMap(String key, Object value) {
        args.put(key, value);
    }

    public Object getArgsMap(String key) {
        return args.get(key);
    }


}
