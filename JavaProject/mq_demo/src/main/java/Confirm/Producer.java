package Confirm;

import Common.RabbitMQTool;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * Common.User: 沐阳Yo
 * Date: 2024/2/11
 * Time: 22:28
 */
public class Producer {
    public static final String QUEUE_NAME = "demo1";
    private static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
//        publishMessageIndividually();//耗时23382ms
//        publishMessageBatch();//耗时523ms
        publishMessageAsync();
    }

    /**
     * 单独确认模式
     */
    public static void publishMessageIndividually() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQTool.getChannel();

        //进行操作
        {
            //开启发布确认
            channel.confirmSelect();

            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                //服务端返回 false 或超时时间内未返回，生产者可以消息重发(需要自己加逻辑处理)
                boolean ok = channel.waitForConfirms();
                if (!ok) {
                    System.out.println("消息发送失败!");
                    System.out.println("需要人工处理消息:" + message);
                    //发送失败的处理逻辑
                }
            }

            System.out.println("全部发送完成!");
        }

        RabbitMQTool.close(channel);
    }

    /**
     * 批量确认模式
     */
    public static void publishMessageBatch() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQTool.getChannel();

        //进行操作
        {
            //开启发布确认
            channel.confirmSelect();

            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                if (i % 100 == 0) {
                    //每发送 100 条确认一次,这样会导致没办法处理丢失的信息
                    boolean ok = channel.waitForConfirms();
                    //服务端返回 false 或超时时间内未返回
                    if (!ok) {
                        System.out.println("这100条消息中有消息丢失!");
                    }
                }
            }

            System.out.println("全部发送完成!");
        }



        RabbitMQTool.close(channel);
    }

    /**
     * 异步确认模式
     */
    public static void publishMessageAsync() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQTool.getChannel();


        //进行操作
        {
            //开启发布确认
            channel.confirmSelect();

            //记录我们全部发送的消息(由于要配合连续删除,所以没有使用 ConcurrentHashMap)
            ConcurrentSkipListMap<Long, String> map = new ConcurrentSkipListMap<>();


            //添加监听者(新线程)
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                    //成功收到回复的函数
                    if (multiple) {
                        //如果是批量应答,则需要删除 deliveryTag 之前的消息
                        Map.Entry<Long, String> entry = map.pollFirstEntry();//获取头元素
                        long start = entry.getKey();
                        while (start <= deliveryTag) {
                            map.remove(start);//逐个删除
                            start++;
                        }
                    } else {
                        System.out.println(deliveryTag + "消息已经发送成功!");
                        map.remove(deliveryTag);
                    }
                }

                @Override
                public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                    //没有收到回复的函数
                    String s = map.get(deliveryTag);
                    System.out.println("deliveryTag为: " + deliveryTag + "内容为: " + s + " 的消息发送失败!");
                }
            });


            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                //记录要发送的信息
                long nextPublishSeqNo = channel.getNextPublishSeqNo();//下一个要发送的deliveryTag                    System.out.println(deliveryTag + "消息已经发送成功!");
                map.put(nextPublishSeqNo, message);

                //发送消息
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }

            System.out.println("全部发送完成!");
        }

        RabbitMQTool.close(channel);
    }
}
