package com.congxiaoyao.user.controller;

import com.congxiaoyao.user.pojo.User;
import com.congxiaoyao.user.service.def.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public User getUserDetail(@PathVariable Long userId, HttpServletResponse response) throws Exception {
        User userDetail = userService.getUserDetail(userId);
        if (userDetail == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return userDetail;
    }
}
