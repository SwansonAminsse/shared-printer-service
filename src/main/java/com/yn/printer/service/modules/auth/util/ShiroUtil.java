package com.yn.printer.service.modules.auth.util;


import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.crypto.hash.format.ParsableHashFormat;
import org.apache.shiro.crypto.hash.format.Shiro1CryptFormat;

/**
 * 登录密码工具类
 *
 * @author huabiao
 * @create 2023/12/11  10:29
 **/
public class ShiroUtil {

    private final ParsableHashFormat hashFormat = new Shiro1CryptFormat();
    private final DefaultPasswordService passwordService = new DefaultPasswordService();

    public ShiroUtil() {
    }

    /**
     * 密码-加密
     *
     * @param password 密码
     * @return 加密后数据
     * @author huabiao
     * @create 2023/12/11  10:36
     */
    public String encrypt(String password) {
        try {
            this.hashFormat.parse(password);
            return password;
        } catch (IllegalArgumentException var3) {
            return this.passwordService.encryptPassword(password);
        }
    }

    /**
     * 密码-验证
     *
     * @param plaintextPassword 明文密码
     * @param savedPassword     保存到数据库的密码
     * @return true:密码正确;false:密码错误
     * @author huabiao
     * @create 2023/12/11  10:36
     */
    public boolean match(String plaintextPassword, String savedPassword) {
        return this.passwordService.passwordsMatch(plaintextPassword, savedPassword);
    }
}