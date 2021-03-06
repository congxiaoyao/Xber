package com.congxiaoyao.auth.pojo;


import com.congxiaoyao.user.pojo.SimpleUser;

/**
 * Created by Jayce on 2016/10/10.
 * 用于回复客户端的UserInfo
 */
public class LoginInfoRsp {
    private Long userId;
    private String name;
    private String username;
    private String authToken;

    public LoginInfoRsp(SimpleUser user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        return "LoginInfoRsp{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
