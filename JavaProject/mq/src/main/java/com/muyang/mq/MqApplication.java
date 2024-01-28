package com.muyang.mq;

import com.muyang.mq.server.BrokerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class MqApplication {
    public static ConfigurableApplicationContext context;

    public static void main(String[] args) throws IOException {
        context = SpringApplication.run(MqApplication.class, args);
        BrokerServer brokerServer = new BrokerServer(9090);//不能和spring的servlet服务一个端口
        brokerServer.start();
    }

}
