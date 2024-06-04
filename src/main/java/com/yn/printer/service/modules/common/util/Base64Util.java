package com.yn.printer.service.modules.common.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Base64Util {
    public void decodeImage(String base64ImageString, String outputFilePath) throws IOException {
        // 将base64编码的字符串解码为字节数组
        byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64ImageString);

        // 将字节数组写入到文件中
        try (OutputStream outputStream = new FileOutputStream(outputFilePath)) {
            outputStream.write(imageBytes);
        }
    }
}
