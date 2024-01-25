package com.example.demo240125_2.demo;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/scope")
@RestController
@Slf4j
public class BeanScopeController {
    @Autowired
    private ApplicationContext context;

    @Resource(name = "singleUser")
    private User singleUser;

    @Resource(name = "prototypeUser")
    private User prototypeUser;

    @Resource(name = "requestUser")
    private User requestUser;

    @Resource(name = "sessionUser")
    private User sessionUser;

    @Resource(name = "applicationUser")
    private User applicationUser;


    @RequestMapping("/single")
    public String single() {
        User user = (User) context.getBean("singleUser");
        log.info("\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + singleUser);
        return "\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + singleUser;
    }

    @RequestMapping("/prototype")
    public String prototype() {
        User user = (User) context.getBean("prototypeUser");
        log.info("\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + prototypeUser);
        return "\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + prototypeUser;
    }

    @RequestMapping("/request")
    public String request() {
        User user = (User) context.getBean("requestUser");
        log.info("\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + requestUser);
        return "\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + requestUser;
    }

    @RequestMapping("/session")
    public String session() {
        User user = (User) context.getBean("sessionUser");
        log.info("\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + sessionUser);
        return "\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + sessionUser;
    }


    @RequestMapping("/application")
    public String application() {
        User user = (User) context.getBean("applicationUser");
        log.info("\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + applicationUser);
        return "\ncontext获取的对象:" + user + "\n属性注入获取的对象:" + applicationUser;
    }


}
