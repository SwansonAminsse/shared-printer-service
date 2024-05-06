package com.yn.printer.service.modules.orders.controller;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.yn.printer.service.modules.common.api.tz.dto.TzPayResultCallback;
import com.yn.printer.service.modules.common.api.wx.config.WeChatApiConfig;
import com.yn.printer.service.modules.common.api.wx.utils.AesUtil;
import com.yn.printer.service.modules.orders.enums.TransactionStatus;
import com.yn.printer.service.modules.orders.service.IOrderService;
import com.yn.printer.service.modules.orders.vo.PayInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Validated
@Api(value = "OrderController", tags = "公共端-订单")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    IOrderService orderService;

    @Autowired
    WeChatApiConfig weChatApiConfig;

    @ApiOperation(value = "订单-天章支付回调")
    @PostMapping("/tzPayCallback")
    public JSONObject tzPayCallback(@RequestBody TzPayResultCallback callback) {
        orderService.handleTzPayCallback(callback);

        JSONObject meta = new JSONObject();
        meta.put("meta", new JSONObject());
        meta.getJSONObject("meta").put("ret_code", "0000");
        return meta;
    }

//    @ApiOperation(value = "订单-余额支付")
//    @PostMapping("/balancePay")
//    public BalancePayVO balancePay(@RequestParam Long orderId) {
//        return orderService.yuEPay(orderId);
//    }

    @ApiOperation(value = "订单-微信支付回调")
    @PostMapping(value = "/wxPayCallback")
    public Map<String, String> wxPayCallback(@RequestBody JSONObject jsonObject) {
        log.info("微信支付回调 -> {}", jsonObject.toJSONString());

        try {
            String key = weChatApiConfig.apiV3key;
            String json = jsonObject.toString();
            String associated_data = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.associated_data");
            String ciphertext = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.ciphertext");
            String nonce = (String) JSONUtil.getByPath(JSONUtil.parse(json), "resource.nonce");

            String decryptData = new AesUtil(key.getBytes(StandardCharsets.UTF_8))
                    .decryptToString(associated_data.getBytes(StandardCharsets.UTF_8), nonce.getBytes(StandardCharsets.UTF_8), ciphertext);
            //验签成功
            JSONObject decryptDataObj = JSONObject.parseObject(decryptData, JSONObject.class);
            orderService.handleWxPayCallback(decryptDataObj.getString("out_trade_no"));

            //decryptDataObj 为解码后的obj，其内容如下。之后便是验签成功后的业务处理
            //{
            //	"sp_appid": "wx8888888888888888",
            //	"sp_mchid": "1230000109",
            //	"sub_appid": "wxd678efh567hg6999",
            //	"sub_mchid": "1900000109",
            //	"out_trade_no": "1217752501201407033233368018",
            //	"trade_state_desc": "支付成功",
            //	"trade_type": "MICROPAY",
            //	"attach": "自定义数据",
            //	"transaction_id": "1217752501201407033233368018",
            //	"trade_state": "SUCCESS",
            //	"bank_type": "CMC",
            //	"success_time": "2018-06-08T10:34:56+08:00",
            //    ...
            //	"payer": {
            //		"openid": "oUpF8uMuAJO_M2pxb1Q9zNjWeS6o"
            //	},
            //	"scene_info": {
            //		"device_id": "013467007045764"
            //	}
            //}
        } catch (Exception e) {
            log.error("处理微信支付通知异常 -> {} , {}", jsonObject.toJSONString(), e);
        }

        Map<String, String> res = new HashMap<>();
        res.put("code", "SUCCESS");
        res.put("message", "成功");
        return res;
    }

    @ApiOperation(value = "订单-获取支付二维码")
    @GetMapping("/getPayQrCode")
    public PayInfoVo getPayQrCode(@RequestParam Long orderId) {
        return orderService.getPayQrCode(orderId);
    }
    @ApiOperation(value = "订单-获取订单状态")
    @GetMapping("/getTransactionStatus")
    public TransactionStatus getTransactionStatus(@RequestParam String code) {
        return orderService.getTransactionStatus(code);
    }

}
