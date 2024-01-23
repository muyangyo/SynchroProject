package com.muyang.mq.server.brokercore;

import com.muyang.mq.common.ConsumerEnv;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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

    // 这些是用来帮助消费的
    // 当前队列都有哪些订阅者
    private List<ConsumerEnv> consumerEnvList = new ArrayList<>();
    // 记录当前取到了第几个消费者. 方便实现轮询策略.(使用原子类保证线程安全)
    private AtomicInteger consumerSeq = new AtomicInteger(0);

    /**
     * 添加订阅泽
     *
     * @param consumerEnv 订阅者
     */
    public void addConsumerEnv(ConsumerEnv consumerEnv) {
        consumerEnvList.add(consumerEnv);
    }

    /**
     * 选择一个订阅者,发消息用
     */
    public ConsumerEnv chooseConsumer() {
        if (consumerEnvList.size() == 0) {
            // 该队列没有人订阅
            return null;
        }
        // 计算一下当前要取的元素的下标
        int index = consumerSeq.get() % consumerEnvList.size();//避免越界
        consumerSeq.incrementAndGet();
        return consumerEnvList.get(index);
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
