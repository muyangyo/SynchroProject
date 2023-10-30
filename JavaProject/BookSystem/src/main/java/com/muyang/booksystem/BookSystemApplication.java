package com.muyang.booksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookSystemApplication.class, args);
        System.out.println("===================成功启动===================");
    }

}
