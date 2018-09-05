package com.congxiaoyao.test.controller;

import com.congxiaoyao.auth.pojo.LoginInfoRsp;
import com.congxiaoyao.auth.token.AuthenticateInfo;
import com.congxiaoyao.test.pojo.TestPO;
import com.congxiaoyao.user.pojo.SimpleUser;
import com.congxiaoyao.user.service.def.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by Jaycejia on 2016/12/8.
 */
@RestController
public class TestController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public TestPO echo(@RequestBody TestPO testPO) {
        return testPO;//echo
    }

    @RequestMapping("/testLogin")
    public String loginTest(HttpServletRequest request) {
//        userService.
        return "hello";
    }
}
