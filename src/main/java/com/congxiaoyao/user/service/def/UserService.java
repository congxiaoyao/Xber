package com.congxiaoyao.user.service.def;

import com.congxiaoyao.user.pojo.SimpleUser;
import com.congxiaoyao.user.pojo.User;
import org.apache.shiro.authc.UnknownAccountException;

/**
 * Created by Jaycejia on 2016/12/4.
 */
public interface UserService {
    /**
     * 通过id获取用户基本信息（用户名密码等）
     * @param userId    用户id
     * @return
     * @throws UnknownAccountException
     */
    SimpleUser getUser(Long userId) throws UnknownAccountException;

    /**
     * 通过用户名获取用户基本信息（用户名密码等）
     * @param username  用户名
     * @return
     * @throws UnknownAccountException
     */
    SimpleUser getUserByName(String username) throws UnknownAccountException;

    /**
     * 获取用户详细信息
     * @param userId
     * @return
     * @throws Exception
     */
    User getUserDetail(Long userId) throws Exception;
}
