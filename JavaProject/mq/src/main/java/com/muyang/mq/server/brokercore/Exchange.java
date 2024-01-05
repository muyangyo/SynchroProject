package com.muyang.mq.server.brokercore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import lombok.Data;

import java.lang.ref.PhantomReference;
import java.util.HashMap;
import java.util.List;
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
public class Exchange {
    private String name;//交换机名称
    private ExchangeType type = ExchangeType.DIRECT;//交换机类型,默认Direct
    private Boolean durable = false;//是否持久化存储
    private Boolean autoDelete = false;//是否自动删除
    private Map<String, Object> args = new HashMap<>();//额外的参数选项

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

    public void setArgs(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.args = objectMapper.readValue(string, new TypeReference<HashMap<String, Object>>() {
            }); //有泛型的类型,必须要用 TypeReference 捕获和操作泛型类型信息
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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