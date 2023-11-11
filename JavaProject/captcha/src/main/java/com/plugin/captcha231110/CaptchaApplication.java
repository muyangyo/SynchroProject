package com.plugin.captcha231110;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CaptchaApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(CaptchaApplication.class, args);
    }

}
