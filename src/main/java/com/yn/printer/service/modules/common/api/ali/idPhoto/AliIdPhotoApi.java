package com.yn.printer.service.modules.common.api.ali.idPhoto;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.yn.printer.service.modules.common.api.ali.idPhoto.request.IdPhotoMakeRequest;
import com.yn.printer.service.modules.common.api.ali.idPhoto.response.IdPhotoMakeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : Jonas Chan
 * @since : 2024/2/1 14:13
 */
@Slf4j
@Component
public class AliIdPhotoApi {

    @Value("${api.ali.idPhoto.makeUrl}")
    String makeUrl;

    public IdPhotoMakeResponse make(IdPhotoMakeRequest request, String appCode) {
        HttpRequest httpRequest = HttpUtil.createPost(makeUrl);
        httpRequest.header("Authorization", "APPCODE " + appCode);
        httpRequest.body(JSON.toJSONString(request));

        log.info("调用阿里云接口生成证件照 >>> {}", makeUrl);
        String res = httpRequest.execute().body();
        log.info("调用阿里云接口生成证件照 <<< {}", res);

        return JSON.parseObject(res).getObject("data", IdPhotoMakeResponse.class);
    }

}
