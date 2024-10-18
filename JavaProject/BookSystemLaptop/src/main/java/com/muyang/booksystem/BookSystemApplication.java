package com.muyang.booksystem;

import com.muyang.booksystem.service.StartPrinting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

//@SpringBootApplication
public class BookSystemApplication {

    public static void main(String[] args) throws UnknownHostException {
        ApplicationContext applicationContext = SpringApplication.run(BookSystemApplication.class, args);
        System.out.println("===================成功启动===================");
        StartPrinting bean = applicationContext.getBean(StartPrinting.class);
        bean.defaultPrint();
    }

}
