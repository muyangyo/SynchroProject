package com.muyang.mq.server;

import com.muyang.mq.common.MqException;
import com.muyang.mq.server.brokercore.BasicProperties;
import com.muyang.mq.server.brokercore.Binding;
import com.muyang.mq.server.brokercore.Msg;
import com.muyang.mq.server.brokercore.Router;
import com.muyang.mq.server.brokercore.constant.ExchangeType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RouterTests {
    private Router router = new Router();
    private Binding binding;
    private Msg message;
    private BasicProperties basicProperties;

    @BeforeEach
    public void setUp() {
        binding = new Binding();
        message = new Msg();
        basicProperties = new BasicProperties();
        message.setBasicProperties(basicProperties);
    }

    @AfterEach
    public void tearDown() {
        binding = null;
        message = null;
        basicProperties = null;
    }

    // [测试用例]
    // binding key          routing key         result
    // aaa                  aaa                 true
    // aaa.bbb              aaa.bbb             true
    // aaa.bbb              aaa.bbb.ccc         false
    // aaa.bbb              aaa.ccc             false
    // aaa.bbb.ccc          aaa.bbb.ccc         true
    // aaa.*                aaa.bbb             true
    // aaa.*.bbb            aaa.bbb.ccc         false
    // *.aaa.bbb            aaa.bbb             false
    // #                    aaa.bbb.ccc         true
    // aaa.#                aaa.bbb             true
    // aaa.#                aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.ccc             true
    // aaa.#.ccc            aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.aaa.bbb.ccc     true
    // #.ccc                ccc                 true
    // #.ccc                aaa.bbb.ccc         true
    @Test
    public void test1() throws MqException {
        binding.setBindingKey("aaa");
        message.getBasicProperties().setRoutingKey("aaa");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test2() throws MqException {
        binding.setBindingKey("aaa.bbb");
        message.getBasicProperties().setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test3() throws MqException {
        binding.setBindingKey("aaa.bbb");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test4() throws MqException {
        binding.setBindingKey("aaa.bbb");
        message.getBasicProperties().setRoutingKey("aaa.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test5() throws MqException {
        binding.setBindingKey("aaa.bbb.ccc");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test6() throws MqException {
        binding.setBindingKey("aaa.*");
        message.getBasicProperties().setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test7() throws MqException {
        binding.setBindingKey("aaa.*.bbb");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test8() throws MqException {
        binding.setBindingKey("*.aaa.bbb");
        message.getBasicProperties().setRoutingKey("aaa.bbb");
        Assertions.assertFalse(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test9() throws MqException {
        binding.setBindingKey("#");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test10() throws MqException {
        binding.setBindingKey("aaa.#");
        message.getBasicProperties().setRoutingKey("aaa.bbb");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test11() throws MqException {
        binding.setBindingKey("aaa.#");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test12() throws MqException {
        binding.setBindingKey("aaa.#.ccc");
        message.getBasicProperties().setRoutingKey("aaa.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test13() throws MqException {
        binding.setBindingKey("aaa.#.ccc");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test14() throws MqException {
        binding.setBindingKey("aaa.#.ccc");
        message.getBasicProperties().setRoutingKey("aaa.aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test15() throws MqException {
        binding.setBindingKey("#.ccc");
        message.getBasicProperties().setRoutingKey("ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }

    @Test
    public void test16() throws MqException {
        binding.setBindingKey("#.ccc");
        message.getBasicProperties().setRoutingKey("aaa.bbb.ccc");
        Assertions.assertTrue(router.route(ExchangeType.TOPIC, binding, message));
    }
}
