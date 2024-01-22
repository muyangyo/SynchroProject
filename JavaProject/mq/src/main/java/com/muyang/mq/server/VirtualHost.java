package com.muyang.mq.server;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.*;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import com.muyang.mq.server.dao.DiskDataManager;
import com.muyang.mq.server.dao.MemoryManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通过这个类, 来表示 虚拟主机(使其具有隔离特性:使用<strong> 虚拟主机名 + 交换机名字 </strong>和<strong> 虚拟主机名 + 队列名字 </strong>实现隔离,
 * 由于绑定是根据 交换机名 和 队列名 决定的,而 交换机名 和 队列名 在不同虚拟主机下是不同名的,所以自动隔离,信息id是必然不同的)
 * 每个虚拟主机下面都管理着自己的 交换机, 队列, 绑定, 消息数据
 * 同时提供 api 供上层调用.
 * 作为业务逻辑的整合者, 就需要对于代码中抛出的异常进行处理了
 */
@Slf4j
public class VirtualHost {
    private String virtualHostName;//虚拟机名称
    private MemoryManager memoryManager;
    private DiskDataManager diskDataManager;
    private Router router;
    private final Object exchangeLocker = new Object();//交换机锁
    private final Object queueLocker = new Object();//队列锁

    public VirtualHost(String virtualHostName) {
        this.virtualHostName = virtualHostName;
        this.diskDataManager = new DiskDataManager();
        this.memoryManager = new MemoryManager();
        this.router = new Router();

        //恢复全部数据到硬盘上
        try {
            memoryManager.recovery(diskDataManager);
        } catch (IOException | MqException | ClassNotFoundException e) {
            e.printStackTrace();
            log.error("恢复数据到内存失败!");
        }
    }

    public String getVirtualHostName() {
        return virtualHostName;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public DiskDataManager getDiskDataManager() {
        return diskDataManager;
    }

    /**
     * 创建一个交换机. 如果交换机不存在, 就创建. 如果存在, 直接返回.
     *
     * @param exchangeName 交换机实际名字
     * @param exchangeType 交换机类型
     * @param durable      是否持久化
     * @param autoDelete   是否自动删除
     * @param arguments    其他参数
     * @return 如果成功创建/已存在返回 true,否则返回 false
     */
    public boolean exchangeDeclare(String exchangeName, ExchangeType exchangeType, boolean durable, boolean autoDelete, Map<String, Object> arguments) {
        exchangeName = virtualHostName + exchangeName;//为了实现虚拟主机的隔离性
        //看看是否已经有这个交换机
        Exchange oldExchange = memoryManager.getExchange(exchangeName);
        try { //为了避免出错,使用try包裹下,如果中途有任何异常,则可以直接告知使用者

            synchronized (exchangeLocker) {
                if (oldExchange != null) {
                    log.info(exchangeName + "交换机成功创建!");
                    return true;//有则直接返回true
                }
                //没有则进行创建
                Exchange exchange = new Exchange();
                exchange.setName(exchangeName);
                exchange.setType(exchangeType);
                exchange.setDurable(durable);
                exchange.setAutoDelete(autoDelete);
                exchange.setArgs(arguments);

                //进行持久化
                if (durable) {
                    diskDataManager.insertExchange(exchange);
                }

                //放入内存中
                memoryManager.insertExchange(exchange);
                log.info(exchangeName + "交换机成功创建!");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(exchangeName + "交换机创建失败!请检查错误!");
            return false;
        }

    }


    /**
     * 删除交换机
     *
     * @param exchangeName 要删除的交换机名
     * @return 成功删除返回 true ,不存在或者删除失败 返回 false
     */
    public boolean exchangeDelete(String exchangeName) {
        exchangeName = virtualHostName + exchangeName;
        try {

            synchronized (exchangeLocker) {
                // 1. 先找到对应的交换机.
                Exchange toDelete = memoryManager.getExchange(exchangeName);
                if (toDelete == null) {
                    log.warn("不存在该名称 {} 的交换机!删除失败!", exchangeName);
                    return false;
                }
                // 2. 删除硬盘上的数据
                if (toDelete.getDurable()) {
                    diskDataManager.deleteExchange(exchangeName);//持久化过则删除
                }
                // 3. 删除内存中的交换机数据
                memoryManager.deleteExchange(exchangeName);
                log.info("{} 交换机删除成功! ", exchangeName);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{} 交换机删除失败! ", exchangeName);
            return false;
        }
    }


    /**
     * 创建队列
     *
     * @param queueName  实际队列名
     * @param durable    是否持久化
     * @param exclusive  是否独占
     * @param autoDelete 是否自动删除
     * @param arguments  其他参数
     * @return 存在/创建成功返回 true ,否则返回 false
     */
    public boolean queueDeclare(String queueName, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments) {
        queueName = virtualHostName + queueName;//为了实现虚拟主机的隔离性
        try {

            synchronized (queueLocker) {
                // 判定队列是否存在
                QueueCore existsQueue = memoryManager.getQueue(queueName);
                if (existsQueue != null) {
                    log.info(queueName + "队列成功创建!");
                    return true;
                }
                // 创建队列对象
                QueueCore queue = new QueueCore();
                queue.setName(queueName);
                queue.setDurable(durable);
                queue.setExclusive(exclusive);
                queue.setAutoDelete(autoDelete);
                queue.setArgs(arguments);
                // 3. 写硬盘
                if (durable) {
                    diskDataManager.insertQueue(queue);
                }
                // 4. 写内存
                memoryManager.insertQueue(queue);
                log.info(queueName + "队列成功创建!");
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(queueName + "队列创建失败!请检查错误!");
            return false;
        }

    }

    /**
     * 删除队列
     *
     * @param queueName 要删除的队列名
     * @return 成功删除返回 true ,不存在或者删除失败 返回 false
     */
    public boolean queueDelete(String queueName) {
        queueName = virtualHostName + queueName;
        try {
            synchronized (queueLocker) {

                // 1. 先找到对应的队列
                QueueCore toDelete = memoryManager.getQueue(queueName);
                if (toDelete == null) {
                    log.warn("不存在该名称 {} 的队列!删除失败!", queueName);
                    return false;
                }
                // 2. 删除硬盘上的数据
                if (toDelete.getDurable()) {
                    diskDataManager.deleteExchange(queueName);//持久化过则删除
                }
                // 3. 删除内存中的交换机数据
                memoryManager.deleteQueue(queueName);
                log.info("{} 队列删除成功! ", queueName);

            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{} 队列删除失败! ", queueName);
            return false;
        }
    }

    /**
     * 创建绑定
     *
     * @param queueName    要绑定的队列名
     * @param exchangeName 要绑定规定交互机名
     * @param bindingKey   口令
     * @return <p>以下情况返回 false ,其他情况返回 true</p>
     * <p>1.要绑定的交换机或者队列不存在</p>
     * <p>2.已经有该绑定</p>
     * <p>3.口令不合法</p>
     */
    public boolean queueBind(String queueName, String exchangeName, String bindingKey) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        try {
            synchronized (exchangeLocker) {
                synchronized (queueLocker) {
                    // 1. 判定当前的绑定是否已经存在
                    Binding existsBinding = memoryManager.getBinding(exchangeName, queueName);
                    if (existsBinding != null) {
                        log.error("当前 {} 绑定已经存在!", existsBinding);
                        return false;
                    }

                    // 2. 验证 bindingKey 是否合法
                    if (!router.checkBindingKey(bindingKey)) {
                        throw new MqException("bindingKey 非法! bindingKey=" + bindingKey);
                    }

                    // 3. 获取一下对应的交换机和队列. 如果交换机或者队列不存在, 则无法创建
                    QueueCore queue = memoryManager.getQueue(queueName);
                    if (queue == null) {
                        log.error("{} 队列不存在!无法创建绑定", queueName);
                        return false;
                    }
                    Exchange exchange = memoryManager.getExchange(exchangeName);
                    if (exchange == null) {
                        log.error("{} 交换机不存在!无法创建绑定", exchangeName);
                        return false;
                    }

                    // 4. 创建 Binding 对象
                    Binding binding = new Binding();
                    binding.setExchangeName(exchangeName);
                    binding.setQueueName(queueName);
                    binding.setBindingKey(bindingKey);

                    // 5. 先写硬盘
                    if (queue.getDurable() && exchange.getDurable()) {
                        diskDataManager.insertBinding(binding);
                    }
                    // 6. 写入内存
                    memoryManager.insertBinding(binding);
                    log.info("{} 绑定创建成功!", binding);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("{} 与 {} 的 绑定创建失败! ", exchangeName, queueName);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除绑定
     *
     * @param queueName    绑定的队列名
     * @param exchangeName 绑定的交换机名
     * @return <strong> 绑定存在 </strong>则无论是否有这个队列或者交换机都返回 ture</p>
     * <p>绑定不存在返回 false</p>
     */
    public boolean queueUnbind(String queueName, String exchangeName) {
        queueName = virtualHostName + queueName;
        exchangeName = virtualHostName + exchangeName;
        try {
            synchronized (exchangeLocker) {
                synchronized (queueLocker) {
                    // 1. 获取 binding 看看是否有
                    Binding binding = memoryManager.getBinding(exchangeName, queueName);
                    if (binding == null) {
                        log.error("{} 与 {} 的绑定删除失败!", exchangeName, queueName);
                    }
                    // 2. 无论绑定是否持久化了, 都尝试从硬盘删一下. 就算不存在, 这个删除也无副作用
                    // 绑定 的持久化是根据 队列 和 交换机 的持久化共同决定的,如果要判断是否持久化,还需要去判断 队列 和 交换机 是否存在.
                    // 这样做的话,就需要 获取 队列 和 交换机,而且必须按照 先删除 绑定 再删除 队列 再删除 交换机 这样的顺序去操作(类似 MySQL的外键)
                    // 虽然这样做更严谨, 但是操作起来麻烦.我们还是直接删,就算没有持久化或者说别的情况都不会产生负面影响(就类似没有外键约束一样比较方便)
                    diskDataManager.deleteBinding(binding);
                    // 3. 删除内存的数据
                    memoryManager.deleteBinding(binding);
                    log.info("{} 绑定删除成功!", binding);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("{} 与 {} 的绑定删除失败!", exchangeName, queueName);
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 发送消息到 指定的交换机的 指定的队列 中
     *
     * @param exchangeName    交换机名
     * @param routingKey      钥匙<strong>(如果是直接交换机,则是交换机名)</strong>
     * @param basicProperties 信息属性
     * @param body            信息载荷
     * @return 成功发送返回 true
     */
    public boolean basicPublish(String exchangeName, String routingKey, BasicProperties basicProperties, byte[] body) {
        try {
            exchangeName = virtualHostName + exchangeName;
            // 1. 检查 routingKey 是否合法 (上面 创建绑定 的时候检查了 bindingKey 的合法性)
            if (!router.checkRoutingKey(routingKey)) {
                throw new MqException("routingKey 非法! routingKey=" + routingKey);
            }
            // 2. 查找交换机对象
            Exchange exchange = memoryManager.getExchange(exchangeName);
            if (exchange == null) {
                throw new MqException(exchangeName + " 交换机不存在!无法发送消息!");
            }
            // 3. 判定 交换机 的类型后再进行 消息 转发
            if (exchange.getType() == ExchangeType.DIRECT) {
                // direct 交换机
                // 直接交换机 以 routingKey 作为队列的名字, 直接把消息写入 routingKey 名字的队列中
                String queueName = virtualHostName + routingKey;
                // 查找该队列名对应的对象
                QueueCore queue = memoryManager.getQueue(queueName);
                if (queue == null) {
                    throw new MqException(queueName + " 队列不存在!无法发送消息!");
                }
                // 构造消息对象
                Msg message = Msg.createMsgWithId(routingKey, basicProperties, body);

                // 给队列中写入消息
                sendMessage(queue, message);
            } else {
                //  fanout 和 topic 的交换机
                ConcurrentHashMap<String, Binding> bindingsMap = memoryManager.getBindings(exchangeName);
                //一个一个去遍历,看看这个 交换机 下面有几个 队列 需要 发送消息
                for (Map.Entry<String, Binding> entry : bindingsMap.entrySet()) {
                    //获取到绑定对象, 判定对应的队列是否存在(存在才能发送消息)
                    Binding binding = entry.getValue();
                    QueueCore queue = memoryManager.getQueue(binding.getQueueName());
                    if (queue == null) {
                        // 这里不抛异常,不要因为一个队列的失败, 影响到给其他队列的消息的传输
                        log.warn("basicPublish 发送消息时, 发现 {} 队列不存在!", binding.getQueueName());
                        continue;//直接下一个队列
                    }
                    // 构造消息对象(这个不能放在循环外面,因为每个信息都要是不同的对象)
                    Msg message = Msg.createMsgWithId(routingKey, basicProperties, body);
                    //    判断这个消息是否要传给这个队列
                    //    如果是 fanout, 所有绑定的队列都要转发的
                    //    如果是 topic, 还需要判定下, bindingKey 和 routingKey 是不是匹配
                    if (!router.route(exchange.getType(), binding, message)) {
                        continue;//如果不符合转发规则,则直接下一个队列
                    }
                    // 转发消息给队列
                    sendMessage(queue, message);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息发送失败!");
            return false;
        }
    }


    //上面方法的提取
    private void sendMessage(QueueCore queue, Msg message) throws IOException, MqException {
        // 此处发送消息, 就是把消息写入到 硬盘 和 内存 上.
        int deliverMode = message.getBasicProperties().getDeliverMode();
        // 1 表示不持久化,2 表示持久化(这里不使用Boolean是为了仿照rabbitMq)
        if (deliverMode == 2) {
            diskDataManager.insertMsg(queue, message);
        }
        // 写入内存
        memoryManager.sendMsg(queue, message);
        // TODO: 2024/1/22 这里可能还需要存入 未确定的消息总表
    }

}