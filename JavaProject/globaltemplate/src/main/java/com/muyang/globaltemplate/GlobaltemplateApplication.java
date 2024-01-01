package com.muyang.globaltemplate;

import com.muyang.globaltemplate.global.starter.GetStartPort;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GlobaltemplateApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(GlobaltemplateApplication.class, args);
        GetStartPort port = context.getBean(GetStartPort.class);
        System.out.println("成功启动于:    " + "http://127.0.0.1:" + port);
    }

}
