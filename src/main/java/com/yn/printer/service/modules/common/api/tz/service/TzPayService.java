package com.yn.printer.service.modules.common.api.tz.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yn.printer.service.modules.common.api.tz.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 天章支付-服务
 *
 * @author : Jonas Chan
 * @since : 2023/12/25 15:52
 */
@Slf4j
@Component
public class TzPayService {

    @Value("${api.tz.createOrderUrl}")
    String createOrderUrl;

    @Value("${api.tz.createQrCodeUrl}")
    String createQrCodeUrl;

    @Value("${api.tz.queryPayResultUrl}")
    String queryPayResultUrl;

    public TzCreateOrderResponse createOrder(TzCreateOrderRequest reqDto) {
        String url = createOrderUrl;
        log.info("调用天章支付接口创建订单>>>{}>>>{}", url, JSON.toJSONString(reqDto));
        String res = HttpUtil.post(url, JSON.toJSONString(reqDto), 5000);
        log.info("调用天章支付接口创建订单<<<{}", res);
        return JSONObject.parseObject(JSONObject.parseObject(res).get("result").toString(), TzCreateOrderResponse.class);
    }

    public TzCreateQrCodeResponse createQrCode(TzCreateQrCodeRequest reqDto) {
        String url = createQrCodeUrl;
        log.info("调用天章支付接口创建支付二维码>>>{}>>>{}", url, JSON.toJSONString(reqDto));
        String res = HttpUtil.post(url, JSON.toJSONString(reqDto), 5000);
        log.info("调用天章支付接口创建支付二维码<<<{}", res);
        return JSONObject.parseObject(JSONObject.parseObject(res).get("result").toString(), TzCreateQrCodeResponse.class);
    }

    public TzQueryPayResultResponse queryPayResult(TzQueryPayResultRequest reqDto) {
        String url = queryPayResultUrl;
        log.info("调用天章支付接口查询支付结果>>>{}>>>{}", url, JSON.toJSONString(reqDto));
        String res = HttpUtil.post(url, JSON.toJSONString(reqDto), 5000);
        log.info("调用天章支付接口查询支付结果<<<{}", res);
        return JSONObject.parseObject(JSONObject.parseObject(res).get("result").toString(), TzQueryPayResultResponse.class);
    }

}
