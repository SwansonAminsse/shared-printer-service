package com.yn.printer.service.modules.common.util;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.yn.printer.service.modules.common.api.wx.dto.PhoneInfo;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

public class WeChatUtil {

    public static PhoneInfo decryptPhoneNumber(String keyStr, String ivStr, String encDataStr) {
        byte[] encData = Base64.decode(encDataStr);
        byte[] iv = Base64.decode(ivStr);
        byte[] key = Base64.decode(keyStr);

        // 如果密钥不足16位，那么就补足
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + 1;
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }

        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            String json = new String(cipher.doFinal(encData), StandardCharsets.UTF_8);
            return JSON.parseObject(json, PhoneInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
