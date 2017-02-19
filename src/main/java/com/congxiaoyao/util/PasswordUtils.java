package com.congxiaoyao.util;

import com.congxiaoyao.auth.pojo.EncryptedPassword;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * Created by Jaycejia on 2017/2/18.
 */
public class PasswordUtils {
    /**
     * 将明文密码加密
     * @param password
     * @return
     */
    public static EncryptedPassword encrypt(String password) {
        SecureRandomNumberGenerator saltGenerator = new SecureRandomNumberGenerator();
        String salt = saltGenerator.nextBytes().toHex();
        Md5Hash md5Hash = new Md5Hash(password, salt);
        return new EncryptedPassword(md5Hash.toString(), salt);
    }
}
