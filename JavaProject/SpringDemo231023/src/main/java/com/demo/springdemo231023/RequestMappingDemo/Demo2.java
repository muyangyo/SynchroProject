package com.demo.springdemo231023.RequestMappingDemo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.multi.MultiFileChooserUI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2023/10/23
 * Time: 17:55
 */
@RequestMapping("/demo2")
@RestController
public class Demo2 {

    @RequestMapping("/m1")
    public String m1(int id) {
        return "接收到了参数:" + id;
    }

    @RequestMapping("/m2")
    public String m2(String name, Integer id) {
        return "接收到了 name 参数:" + name + " id 参数:" + id;
    }

    @RequestMapping("/m3")
    public String m3(Person person) {
        return "接收到了参数:" + person;
    }

    @RequestMapping("/m4")
    public String m4(@RequestParam("name") String username) {
        return "接收到了参数:" + username;
    }

    @RequestMapping("/m5")
    public String m5(@RequestParam(value = "name", required = false) String username) {
        return "接收到了参数:" + username;
    }

    @RequestMapping("/m6")
    public String m6(String[] array) {
        return "接收到了参数:" + Arrays.toString(array);
    }

    @RequestMapping("/m7")
    public String m7(@RequestParam List<String> list) {
        return "接收到了参数:" + list + " 大小为:" + list.size();
    }

    @RequestMapping("/m8")
    public String m8(@RequestBody(required = false) Person person) {
        return "接收到的JSON对象是:" + person;
    }

    @RequestMapping("/m9/{userId}/{username}")
    public String m9(@PathVariable Integer userId, @PathVariable("username") String name) {
        return "获取到的URL中的参数 userId :" + userId + " name :" + name;
    }

    @RequestMapping("/m10")
    public String m10(@RequestPart MultipartFile file) {
        return "已上传文件:" + file.getName();
    }

    @RequestMapping("/m11")
    public String m11(@RequestParam List<String> list) {
        return "接收到了参数:" + list + " 大小为:" + list.size();
    }

}
