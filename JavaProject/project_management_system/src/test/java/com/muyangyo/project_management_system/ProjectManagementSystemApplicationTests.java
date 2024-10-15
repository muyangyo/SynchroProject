package com.muyangyo.project_management_system;

import com.muyangyo.project_management_system.global.model.User;
import com.muyangyo.project_management_system.global.model.request_model.ForLogin;
import converter.MetaToVoConverter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectManagementSystemApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    void Test() throws Exception {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setNickname("nickname");
        //new出一个vo对象
        ForLogin forLogin = new ForLogin();
        forLogin.setUsername(user.getUsername());
        forLogin.setPassword(user.getPassword());

        ForLogin convert = MetaToVoConverter.convert(user, ForLogin.class);

        System.out.println(convert);
    }
}
