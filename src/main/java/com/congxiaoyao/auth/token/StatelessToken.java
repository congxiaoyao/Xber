package com.congxiaoyao.auth.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by Jayce on 2016/9/21.
 * 认证token信息
 */
public class StatelessToken implements AuthenticationToken {
    private String clientId;
    private String accessTokenString;


    @Override
    public Object getPrincipal() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Object getCredentials() {
        return accessTokenString;
    }
    public void setAccessTokenString(String accessTokenString) {
        this.accessTokenString = accessTokenString;
    }




}
