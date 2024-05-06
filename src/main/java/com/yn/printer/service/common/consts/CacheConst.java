package com.yn.printer.service.common.consts;

/**
 * @author huabiao
 * @create 2021/4/22  20:18
 **/
public class CacheConst {

    /**
     * user 登录 token
     */
    public static final String TOKEN_USER = "TOKEN_USER";

    /**
     * user 登录 token
     */
    public static String getTokenUser(String token) {
        return TOKEN_USER + ":" + token;
    }

    /**
     * member 登录 token
     */
    public static final String TOKEN_MEMBER = "TOKEN_MEMBER";

    /**
     * member 登录 token
     */
    public static String getTokenMember(String token) {
        return TOKEN_MEMBER + ":" + token;
    }

    public static String getPhoneNumberKey(String token) {
        return "PHONE_NUMBER:" + token;
    }
}
