package com.example.demo240125_2.demo;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class BeanConfig {

    @Bean
    public User user1() {
        return new User(1, "zhangsan");
    }


    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Bean
    public User singleUser() {
        return new User();
    }


    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public User prototypeUser() {
        return new User();
    }


    @RequestScope
    @Bean("requestUser")
    public User requestUser() {
        return new User();
    }


    @SessionScope
    @Bean("sessionUser")
    public User sessionUser() {
        return new User();
    }


    @ApplicationScope
    @Bean("applicationUser")
    public User applicationUser() {
        return new User();
    }
}
