package com.example.demo240125_2.demo;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.BeanNameAware;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/1/25
 * Time: 20:14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User implements BeanNameAware {
    private Integer id;
    private String name;

    @Override
    public void setBeanName(String name) {
        System.out.println("setBeanName:" + name);
    }
}
