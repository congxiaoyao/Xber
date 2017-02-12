package com.congxiaoyao.auth.pojo;

import java.util.Date;

public class StatelessSession {
    private String clientId;

    private Long userId;

    private String token;

    private Date loginTime;

    private Date lastActiveTime;

    public StatelessSession() {
    }

    public StatelessSession(String clientId, Long userId, String token, Date loginTime, Date lastActiveTime) {
        this.clientId = clientId;
        this.userId = userId;
        this.token = token;
        this.loginTime = loginTime;
        this.lastActiveTime = lastActiveTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId == null ? null : clientId.trim();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }
}