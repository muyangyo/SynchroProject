package com.demo.springdemo231023;

import com.demo.springdemo231023.Configs.Config;
import com.demo.springdemo231023.IoC.Demo;
import com.demo.springdemo231023.IoC.Extension;
import com.demo.springdemo231023.IoC.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringDemo231023Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(SpringDemo231023Application.class, args);
        System.out.println();

        /*Extension bean1 = applicationContext.getBean("extensionDemo1", Extension.class);
        bean1.run();*/

        /*Extension bean2 = applicationContext.getBean("extensionDemo2", Extension.class);
        bean2.run();*/

        /*System.out.println(bean1 == bean2);*/


        /*Demo bean = applicationContext.getBean(Demo.class);
        bean.ex1.run();*/


        /*Test bean = applicationContext.getBean(Test.class);
        bean.extension.run();*/

        Config bean = applicationContext.getBean(Config.class);
        bean.outConfig();
    }

}
