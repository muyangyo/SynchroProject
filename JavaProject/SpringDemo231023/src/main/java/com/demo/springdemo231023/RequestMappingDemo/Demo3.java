package com.demo.springdemo231023.RequestMappingDemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/27
 * Time: 9:44
 */
@Controller
@RequestMapping("/demo3")
public class Demo3 {

    @RequestMapping("/getIndex")
    public Object getIndex() {
        return "/index.html";
    }

    @RequestMapping("/getHTML")
    @ResponseBody
    public String getHTML() {
        return "<h1>HTML代码</h1>";
    }

    @RequestMapping("/getJSON")
    @ResponseBody
    public Object getJSON() {
        return new Person();
    }

    @RequestMapping("/setStatus")
    @ResponseBody
    public String setStatus(HttpServletResponse response) {
        response.setStatus(401);
        return "设置状态码成功";
    }

    @RequestMapping(value = "/setHeader", produces = "text/html;charset=UTF8")
    @ResponseBody
    public String setHeader() {
        return "设置 header 成功";
    }


}
