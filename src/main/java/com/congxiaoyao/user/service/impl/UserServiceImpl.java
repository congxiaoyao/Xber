package com.congxiaoyao.user.service.impl;

import com.congxiaoyao.auth.pojo.EncryptedPassword;
import com.congxiaoyao.user.dao.SimpleUserMapper;
import com.congxiaoyao.user.dao.UserMapper;
import com.congxiaoyao.user.pojo.SimpleUser;
import com.congxiaoyao.user.pojo.SimpleUserExample;
import com.congxiaoyao.user.pojo.User;
import com.congxiaoyao.user.service.def.UserService;
import com.congxiaoyao.util.PasswordUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Jaycejia on 2016/12/4.
 */
@Transactional
@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private SimpleUserMapper simpleUserMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public SimpleUser getUser(Long userId) throws UnknownAccountException {
        SimpleUser simpleUser = simpleUserMapper.selectByPrimaryKey(userId);
        if (simpleUser == null) {
            throw new UnknownAccountException();
        }
        return simpleUser;
    }

    @Override
    public SimpleUser getUserByName(String username) throws UnknownAccountException {
        SimpleUserExample example = new SimpleUserExample();
        SimpleUserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<SimpleUser> simpleUsers = simpleUserMapper.selectByExample(example);
        if (simpleUsers == null || simpleUsers.size() == 0) {//若找不到用户则无此用户
            throw new UnknownAccountException("找不到该用户");
        } else {
            return simpleUsers.get(0);
        }
    }

    @Override
    public User getUserDetail(Long userId) throws UnknownAccountException {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) {
            throw new UnknownAccountException();
        }
        return user;
    }

    /**
     * 注册用户
     *
     * @param user
     */
    @Override
    public void registerUser(User user) {
        EncryptedPassword encryptedPassword = PasswordUtils.encrypt(user.getPassword());
        user.setPassword(encryptedPassword.getPassword());
        user.setSalt(encryptedPassword.getSalt());
        userMapper.insert(user);
    }
}
