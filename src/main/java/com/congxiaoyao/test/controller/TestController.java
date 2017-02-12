package com.congxiaoyao.test.controller;

import com.congxiaoyao.auth.pojo.LoginInfoRsp;
import com.congxiaoyao.test.pojo.TestPO;
import com.congxiaoyao.user.pojo.SimpleUser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by Jaycejia on 2016/12/8.
 */
@RestController
public class TestController {
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public TestPO echo(@RequestBody TestPO testPO) {
        return testPO;//echo
    }

    @RequestMapping("/testLogin")
    public LoginInfoRsp loginTest() {
        SimpleUser user = new SimpleUser();
        user.setUserId(100L);
        user.setName("丛圣杰");
        user.setUsername("congxiaoyao");
        LoginInfoRsp loginInfoRsp = new LoginInfoRsp(user);
        loginInfoRsp.setAuthToken(UUID.randomUUID().toString());
        return loginInfoRsp;
    }
}
