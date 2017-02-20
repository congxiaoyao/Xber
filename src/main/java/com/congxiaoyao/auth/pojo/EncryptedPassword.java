package com.congxiaoyao.auth.pojo;

/**
 * Created by Jaycejia on 2017/2/19.
 */
public class EncryptedPassword {
    private String password;
    private String salt;

    public EncryptedPassword(String password, String salt) {
        this.password = password;
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
