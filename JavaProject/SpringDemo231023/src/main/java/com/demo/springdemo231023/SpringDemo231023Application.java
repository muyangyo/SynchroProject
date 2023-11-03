package com.demo.springdemo231023;

import com.demo.springdemo231023.IOC.Extension;
import com.demo.springdemo231023.IOC.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringDemo231023Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringDemo231023Application.class, args);
        System.out.println("成功启动!");

        Extension bean = applicationContext.getBean(Extension.class);
        bean.run();

    }

}
