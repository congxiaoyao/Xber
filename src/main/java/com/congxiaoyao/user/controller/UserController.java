package com.congxiaoyao.user.controller;

import com.congxiaoyao.user.pojo.User;
import com.congxiaoyao.user.service.def.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Jaycejia on 2017/2/18.
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 注册司机用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String registerDriver(@RequestBody User user) {
        userService.registerUser(user);
        return "操作成功";
    }
}
