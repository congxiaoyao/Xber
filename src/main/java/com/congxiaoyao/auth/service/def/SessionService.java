package com.congxiaoyao.auth.service.def;

import com.congxiaoyao.auth.pojo.StatelessSession;
import com.congxiaoyao.auth.token.AuthenticateInfo;
import org.apache.shiro.authc.AuthenticationException;

/**
 * Created by Jaycejia on 2017/2/10.
 */
public interface SessionService {
    StatelessSession getSessionByClientId(String clientId);

    boolean isExpired(StatelessSession session);

    boolean userHasLogin(Long userId);

    void register(StatelessSession session) throws Exception;

    void refresh(StatelessSession session) throws Exception;

    void clean(StatelessSession session) throws AuthenticationException;

    String registerAccessToken(AuthenticateInfo authcInfo) throws Exception;

}
