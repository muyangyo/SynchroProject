package com.muyang.mq.server;

import com.muyang.mq.MqApplication;
import com.muyang.mq.common.ConsumerBehavior;
import com.muyang.mq.server.brokercore.BasicProperties;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.IOException;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/27
 * Time: 16:52
 */
class VirtualHostTest {
    private VirtualHost virtualHost = null;

    @BeforeEach
    public void setUp() {
        MqApplication.context = SpringApplication.run(MqApplication.class);
        virtualHost = new VirtualHost("default");
    }

    @AfterEach
    public void tearDown() throws IOException {
        MqApplication.context.close();
        virtualHost = null;
        // 把硬盘的目录删除掉
        File dataDir = new File("./data");
        FileUtils.deleteDirectory(dataDir);
    }

    @Test
    public void testExchangeDeclare() {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);
    }

    @Test
    public void testExchangeDelete() {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDelete("testExchange");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueDeclare() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueDelete() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDelete("testQueue");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueBind() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "testBindingKey");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueUnbind() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "testBindingKey");
        Assertions.assertTrue(ok);

        ok = virtualHost.queueUnbind("testQueue", "testExchange");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testBasicPublish() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);
    }

    // 先订阅队列, 后发送消息
    @Test
    public void testBasicConsume1() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先订阅队列
        ok = virtualHost.basicConsume("testConsumerBehaviorTag", "testQueue", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                try {
                    // 消费者自身设定的回调方法.
                    System.out.println("messageId=" + basicProperties.getId());
                    System.out.println("body=" + new String(body, 0, body.length));

                    Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                    Assertions.assertEquals(1, basicProperties.getDeliverMode());
                    Assertions.assertArrayEquals("hello".getBytes(), body);
                } catch (Error e) {
                    // 断言如果失败, 抛出的是 Error, 而不是 Exception!
                    e.printStackTrace();
                    System.out.println("error");
                }
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);

        // 再发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);
    }

    // 先发送消息, 后订阅队列.
    @Test
    public void testBasicConsume2() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);

        // 再订阅队列
        ok = virtualHost.basicConsume("testConsumerBehaviorTag", "testQueue", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                // 消费者自身设定的回调方法.
                System.out.println("messageId=" + basicProperties.getId());
                System.out.println("body=" + new String(body, 0, body.length));

                Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                Assertions.assertEquals(1, basicProperties.getDeliverMode());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicConsumeFanout() throws InterruptedException {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.FANOUT, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue1", false, false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.queueBind("testQueue1", "testExchange", "");
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue2", false, false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.queueBind("testQueue2", "testExchange", "");
        Assertions.assertTrue(ok);

        // 往交换机中发布一个消息
        ok = virtualHost.basicPublish("testExchange", "", null, "hello".getBytes());
        Assertions.assertTrue(ok);

        Thread.sleep(500);

        // 两个消费者订阅上述的两个队列.
        ok = virtualHost.basicConsume("testConsumerBehavior1", "testQueue1", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        ok = virtualHost.basicConsume("testConsumerBehavior2", "testQueue2", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicConsumeTopic() throws InterruptedException {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.TOPIC, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue", false, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "aaa.*.bbb");
        Assertions.assertTrue(ok);

        ok = virtualHost.basicPublish("testExchange", "aaa.ccc.bbb", null, "hello".getBytes());
        Assertions.assertTrue(ok);

        ok = virtualHost.basicConsume("testConsumerBehavior", "testQueue", true, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicAck() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.DIRECT,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);

        // 再订阅队列 [要改的地方, 把 autoAck 改成 false]
        ok = virtualHost.basicConsume("testConsumerBehaviorTag", "testQueue", false, new ConsumerBehavior() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                // 消费者自身设定的回调方法.
                System.out.println("messageId=" + basicProperties.getId());
                System.out.println("body=" + new String(body, 0, body.length));

                Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                Assertions.assertEquals(1, basicProperties.getDeliverMode());
                Assertions.assertArrayEquals("hello".getBytes(), body);

                // [要改的地方, 新增手动调用 basicAck]
                boolean ok = virtualHost.basicAck("testQueue", basicProperties.getId());
                Assertions.assertTrue(ok);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

}