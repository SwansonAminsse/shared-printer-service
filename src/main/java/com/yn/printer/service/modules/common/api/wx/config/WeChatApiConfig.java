package com.yn.printer.service.modules.common.api.wx.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeChatApiConfig {
    @Value("${api.wx.applet.appid}")
    public String appid;
    @Value("${api.wx.applet.secret}")
    public String secret;
    @Value("${api.wx.loginUrl}")
    public String wxLoginUrl;
    @Value("${api.wx.getAccessTokenUrl}")
    public String getAccessTokenUrl;
    @Value("${api.wx.getPhoneNumberUrl}")
    public String getPhoneNumberUrl;
    @Value("${api.wx.pay.mchId}")
    public String mchId;
    @Value("${api.wx.pay.notifyUrl}")
    public String notifyUrl;
    @Value("${api.wx.pay.privateKeyPath}")
    public String privateKeyPath;
    @Value("${api.wx.pay.merchantSerialNumber}")
    public String merchantSerialNumber;
    @Value("${api.wx.pay.apiV3key}")
    public String apiV3key;

    private static Config config = null;

    // 使用自动更新平台证书的RSA配置
    // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源
    public synchronized Config getConfig() {
        if (config == null) {
            config = new RSAAutoCertificateConfig.Builder()
                    .merchantId(mchId)
                    .privateKeyFromPath(privateKeyPath)
                    .merchantSerialNumber(merchantSerialNumber)
                    .apiV3Key(apiV3key)
                    .build();
            System.out.println("config -> register");
            return config;
        } else
            return config;
    }
}
