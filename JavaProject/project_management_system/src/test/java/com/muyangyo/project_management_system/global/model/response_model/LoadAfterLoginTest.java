package com.muyangyo.project_management_system.global.model.response_model;

import com.muyangyo.project_management_system.global.model.Role;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 创建于 IntelliJ IDEA.
 * 描述：
 * User: 沐阳Yo
 * Date: 2024/9/4
 * Time: 14:31
 */
class LoadAfterLoginTest {
    @Test
    public void test() throws IllegalAccessException {
        Method[] declaredMethods = LoadAfterLogin.class.getDeclaredMethods();
        for (Method method : declaredMethods) {
            System.out.println(method.getName());
        }
        System.out.println("==========================================");
        Method[] methods = LoadAfterLogin.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }
        System.out.println("==========================================");
        HashMap<String,String> map = new HashMap<>();
        LoadAfterLogin loadAfterLogin = new LoadAfterLogin("123", Role.ROOT.toString(), "123");
        Field[] fields = LoadAfterLogin.class.getDeclaredFields();
        for (Field tmp : fields) {
            Object o = tmp.get(loadAfterLogin);
            System.out.println(tmp.getName()+" : "+ o);
        }
    }

}