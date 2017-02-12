package com.congxiaoyao.auth.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created by Jayce on 2016/9/22.
 * 登录时需要的信息
 */
public class AuthenticateInfo implements AuthenticationToken {
    private String username;
    private String password;
    private String clientId;

    @Override
    public Object getPrincipal() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

	public AuthenticateInfo(String username, String password, String clientId) {
		super();
		this.username = username;
		this.password = password;
		this.clientId = clientId;
	}

	public AuthenticateInfo() {

	}
    

}
