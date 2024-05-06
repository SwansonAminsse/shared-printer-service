package com.yn.printer.service.modules.common.api.wx.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.wechat.pay.java.core.util.PemUtil;
import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.yn.printer.service.modules.common.api.wx.config.WeChatApiConfig;
import com.yn.printer.service.modules.common.api.wx.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class WeChatApiService {

    @Autowired
    WeChatApiConfig weChatApiConfig;

    public WxLoginResDto login(WxLoginReqDto dto) {
        if (dto.getAppid() == null)
            dto.setAppid(weChatApiConfig.appid);
        if (dto.getSecret() == null)
            dto.setSecret(weChatApiConfig.secret);

        String url = weChatApiConfig.wxLoginUrl + String.format("?appid=%s", dto.getAppid()) +
                String.format("&secret=%s", dto.getSecret()) +
                String.format("&js_code=%s", dto.getJs_code()) +
                String.format("&grant_type=%s", dto.getGrant_type());
        log.info("调用微信接口小程序登录>>>{}", url);
        String res = HttpUtil.get(url, 5000);
        log.info("调用微信接口小程序登录<<<{}", res);

        return JSONObject.parseObject(res, WxLoginResDto.class);
    }

    public WxGetAccessTokenResDto getAccessToken(WxGetAccessTokenReqDto dto) {
        if (dto.getAppid() == null)
            dto.setAppid(weChatApiConfig.appid);
        if (dto.getSecret() == null)
            dto.setSecret(weChatApiConfig.secret);

        String url = weChatApiConfig.getAccessTokenUrl + String.format("?appid=%s", dto.getAppid()) +
                String.format("&secret=%s", dto.getSecret()) +
                String.format("&grant_type=%s", dto.getGrant_type());
        log.info("调用微信接口获取AccessToken>>>{}", url);
        String res = HttpUtil.get(url, 5000);
        log.info("调用微信接口获取AccessToken<<<{}", res);

        return JSONObject.parseObject(res, WxGetAccessTokenResDto.class);
    }

    public WxGetPhoneNumberResDto getPhoneNumber(WxGetPhoneNumberReqDto dto) {
        String url = weChatApiConfig.getPhoneNumberUrl + String.format("?access_token=%s", dto.getAccess_token());
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("code", dto.getCode());
        log.info("调用微信接口获取手机号>>>{} {}", url, JSONObject.toJSONString(map));
        String res = HttpUtil.post(url, JSONObject.toJSONString(map), 5000);
        log.info("调用微信接口获取手机号<<<{}", res);

        return JSONObject.parseObject(res, WxGetPhoneNumberResDto.class);
    }

    public Transaction queryPayOrder(QueryOrderByOutTradeNoRequest request) {
        request.setMchid(weChatApiConfig.mchId);
        log.info("调用微信查询订单状态接口请求 -> {}", request);
        // 构建service
        JsapiService service = new JsapiService.Builder().config(weChatApiConfig.getConfig()).build();
        Transaction response = service.queryOrderByOutTradeNo(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        log.info("调用微信查询订单状态接口返回 -> {}", response);
        return response;
    }

    public PrepayResponse createJsPayOrder(PrepayRequest request) {
        // 小程序appid
        request.setAppid(weChatApiConfig.appid);
        // 商户号
        request.setMchid(weChatApiConfig.mchId);
        // 支付成功回调地址
        request.setNotifyUrl(weChatApiConfig.notifyUrl);

        log.info("调用微信下单接口请求 -> {}", request);
        // 构建service
        JsapiService service = new JsapiService.Builder().config(weChatApiConfig.getConfig()).build();
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        log.info("调用微信下单接口返回 -> {}", response);
        return response;
    }

    public void signByRSA(JsPayInfo jsPayInfo) {
        String message = jsPayInfo.toSignString(weChatApiConfig.appid);
        try {
            //replace 根据实际情况，不一定都需要
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKeyFromPath(weChatApiConfig.privateKeyPath);
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(merchantPrivateKey);
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            jsPayInfo.setPaySign(Base64Utils.encodeToString(sign.sign()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("当前Java环境不支持RSA v1.5/OAEP", e);
        } catch (InvalidKeyException | SignatureException e) {
            throw new IllegalArgumentException("无效的证书", e);
        }

    }

    public String createNativePayOrder(BigDecimal price, String description, String outTradeNo) {
        // 构建service
        NativePayService service = new NativePayService.Builder().config(weChatApiConfig.getConfig()).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest request = new com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest();
        com.wechat.pay.java.service.payments.nativepay.model.Amount amount = new com.wechat.pay.java.service.payments.nativepay.model.Amount();
        amount.setTotal(price.multiply(BigDecimal.valueOf(100)).intValue());
        request.setAmount(amount);
        request.setAppid(weChatApiConfig.appid);
        request.setMchid(weChatApiConfig.mchId);
        request.setDescription(description);
        request.setNotifyUrl(weChatApiConfig.notifyUrl);
        request.setOutTradeNo(outTradeNo);
        // 调用下单方法，得到应答
        log.info("调用微信下单接口请求 -> {}", request);
        com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse response = service.prepay(request);
        log.info("调用微信下单接口返回 -> {}", response);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        return response.getCodeUrl();
    }


    //    public static void main(String[] args) {
//        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
//        request.setMchid(merchantId);
//        request.setOutTradeNo("1232131312323123132123123");
//        queryOrderTest(request);
//    }
//
//    public static String createOrderTest() {
//        // 使用自动更新平台证书的RSA配置
//        // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源
//        Config config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(merchantId)
//                .privateKeyFromPath(privateKeyPath)
//                .merchantSerialNumber(merchantSerialNumber)
//                .apiV3Key(apiV3key)
//                .build();
//        // 构建service
//        NativePayService service = new NativePayService.Builder().config(config).build();
//        // request.setXxx(val)设置所需参数，具体参数可见Request定义
//        PrepayRequest request = new PrepayRequest();
//        Amount amount = new Amount();
//        amount.setTotal(1);
//        request.setAmount(amount);
//        request.setAppid("wx5b66e47c8a23f684");
//        request.setMchid(merchantId);
//        request.setDescription("测试商品标题");
//        request.setNotifyUrl("https://notify_url");
//        request.setOutTradeNo("1232131312323123132123123");
//        // 调用下单方法，得到应答
//        PrepayResponse response = service.prepay(request);
//        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
//        System.out.println(response.getCodeUrl());
//        return response.getCodeUrl();
//    }
//
//    public static Transaction queryOrderTest(QueryOrderByOutTradeNoRequest request) {
//        // 使用自动更新平台证书的RSA配置
//        // 建议将 config 作为单例或全局静态对象，避免重复的下载浪费系统资源
//        Config config = new RSAAutoCertificateConfig.Builder()
//                .merchantId(merchantId)
//                .privateKeyFromPath(privateKeyPath)
//                .merchantSerialNumber(merchantSerialNumber)
//                .apiV3Key(apiV3key)
//                .build();
//        log.info("调用微信查询订单状态接口请求 -> {}", request);
//        // 构建service
//        NativePayService service = new NativePayService.Builder().config(config).build();
//        Transaction response = service.queryOrderByOutTradeNo(request);
//        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
//        log.info("调用微信查询订单状态接口返回 -> {}", response);
//        return response;
//    }

}
