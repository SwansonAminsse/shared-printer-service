package com.yn.printer.service.modules.common.util;

import com.yn.printer.service.modules.common.vo.IDcardRecoVO;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

public class Base64Util {
    public MultipartFile decodeImage(IDcardRecoVO idCardRecoVO) {
        String base64ImageString = null;
        List<IDcardRecoVO.Item> items = idCardRecoVO.getData().getCardsinfo().getCard().getItem();
        for (IDcardRecoVO.Item item : items) {
            if ("处理后的图片".equals(item.getDesc())) {
                String content = item.getContent();
                base64ImageString = content;
            }
        }
        // 去掉 Base64 字符串的前缀（如果有）
        if (base64ImageString.contains(",")) {
            base64ImageString = base64ImageString.split(",")[1];
        }
        // 解码 Base64 字符串
        byte[] decodedBytes = Base64.getDecoder().decode(base64ImageString);
        // 创建 MultipartFile 对象
        MultipartFile multipartFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", decodedBytes);
        return multipartFile;
    }
}
