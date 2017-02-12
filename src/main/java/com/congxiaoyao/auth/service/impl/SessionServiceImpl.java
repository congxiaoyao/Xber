package com.congxiaoyao.auth.service.impl;

import com.congxiaoyao.auth.dao.StatelessSessionMapper;
import com.congxiaoyao.auth.exception.LogOutException;
import com.congxiaoyao.auth.pojo.StatelessSession;
import com.congxiaoyao.auth.pojo.StatelessSessionExample;
import com.congxiaoyao.auth.realm.StatelessRealm;
import com.congxiaoyao.auth.service.def.SessionService;
import com.congxiaoyao.auth.token.AuthenticateInfo;
import com.congxiaoyao.user.service.def.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jaycejia on 2016/12/4.
 */
@Transactional
@Service("sessionService")
@PropertySource("classpath:config-common.properties")
public class SessionServiceImpl implements SessionService {
    @Value("${system.isExpired}")
    private Boolean NO_EXPIRED;
    @Value("${system.expiredTime}")
    private Integer EXPIRED_TIME;
    @Autowired
    private StatelessSessionMapper sessionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private StatelessRealm realm;

    @Override
    public StatelessSession getSessionByClientId(String clientId) {
        return sessionMapper.selectByPrimaryKey(clientId);
    }

    @Override
    public boolean isExpired(StatelessSession session) {
        Calendar expiredDate = Calendar.getInstance(), now = Calendar.getInstance();
        now.setTime(new Date());
        expiredDate.setTime(session.getLastActiveTime());
        expiredDate.add(Calendar.MINUTE, EXPIRED_TIME);
        return now.after(expiredDate) && !NO_EXPIRED;
    }

    @Override
    public boolean userHasLogin(Long userId) {
        return sessionMapper.selectByExample(getExampleByUserId(userId)).size() > 0;
    }

    @Override
    public void register(StatelessSession session) throws Exception {
        clearAuthCache(session);
        if (!userHasLogin(session.getUserId())) {
            sessionMapper.insert(session);
        } else {
            clean(sessionMapper.selectByExample(getExampleBySession(session)).get(0));
            sessionMapper.insert(session);
        }
    }

    @Override
    public void refresh(StatelessSession session) throws Exception {
        session.setLastActiveTime(new Date());
        sessionMapper.updateByExampleSelective(session, getExampleBySession(session));
    }

    @Override
    public void clean(StatelessSession session) throws AuthenticationException {
        clearAuthCache(session);
        if (!(sessionMapper.deleteByExample(getExampleBySession(session)) > 0)) {
            throw new LogOutException();
        }
    }

    @Override
    public String registerAccessToken(AuthenticateInfo authcInfo) throws Exception {
        Date timestamp = new Date();
        Md5Hash token = new Md5Hash(authcInfo.getPrincipal() + (String) authcInfo.getCredentials() + authcInfo.getClientId() + timestamp);
        StatelessSession session = new StatelessSession(authcInfo.getClientId(), userService.getUserByName((String) authcInfo.getPrincipal()).getUserId(), new Md5Hash(token.toString()).toString(), timestamp, timestamp);
        register(session);
        return token.toString();
    }

    /**
     * 初始化查询参数（根据session信息）
     */
    private StatelessSessionExample getExampleBySession(StatelessSession session) {
        StatelessSessionExample example = new StatelessSessionExample();
        StatelessSessionExample.Criteria clientIdCriteria = example.createCriteria();
        StatelessSessionExample.Criteria userIdCriteria = example.createCriteria();
        StatelessSessionExample.Criteria tokenCriteria = example.createCriteria();
        clientIdCriteria.andClientIdEqualTo(session.getClientId());
        tokenCriteria.andTokenEqualTo(session.getToken());
        userIdCriteria.andUserIdEqualTo(session.getUserId());
        example.or(userIdCriteria);
        example.or(tokenCriteria);
        return example;
    }

    /**
     * 初始化查询参数(根据用户id)
     */
    private StatelessSessionExample getExampleByUserId(Long userId) {
        StatelessSessionExample example = new StatelessSessionExample();
        StatelessSessionExample.Criteria userIdCriteria = example.createCriteria();
        userIdCriteria.andUserIdEqualTo(userId);
        return example;
    }

    /**
     * 清理认证缓存
     * @param session
     */
    private void clearAuthCache(StatelessSession session) {
        PrincipalCollection collection = new SimplePrincipalCollection(session.getClientId(), realm.getName());
        realm.doClearCache(collection);
    }
}
