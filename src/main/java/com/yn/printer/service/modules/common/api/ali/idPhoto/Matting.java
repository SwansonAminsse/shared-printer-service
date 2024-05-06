/*
==============================智能人像分割（抠图）接口 人像抠图返回透明背景==============================
*/

package com.yn.printer.service.modules.common.api.ali.idPhoto;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Matting {

    // 智能人像分割（抠图）接口 人像抠图返回透明背景
    private final String MattingAPIServer = "https://matting.market.alicloudapi.com";
    private final String MattingAPIUri = "/segment/matting";
    private final int MaxPhotoEdge = 2000;

    private String base64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    public byte[] resizeAndReadImage(File srcFile) throws IOException {
        ByteArrayOutputStream out = null;
        try {
            // 构造Image对象
            BufferedImage src = ImageIO.read(srcFile);
            int width = src.getWidth();
            int height = src.getHeight();

            int maxEdge = Math.max(width, height);

            // 接口要求图片最长边不超过1500个像素，超过的话，需要先做缩放
            if (maxEdge <= this.MaxPhotoEdge) {
                FileInputStream inputFile = new FileInputStream(srcFile);
                byte[] buffer = new byte[(int) srcFile.length()];
                inputFile.read(buffer);
                inputFile.close();
                return buffer;
            }
            float ratio = (float) this.MaxPhotoEdge / (float) maxEdge;
            int newWidth = (int) ((float) width * ratio);
            int newHeight = (int) ((float) height * ratio);

            BufferedImage tag = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            tag.getGraphics().drawImage(src, 0, 0, newWidth, newHeight, null);

            out = new ByteArrayOutputStream();
            ImageIO.write(tag, "JPEG", out);
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return null;
    }


    private String httpPost(String httpUrl, Map<String, String> headers, String postData) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        try {
            URL url = new URL(httpUrl);

            connection = (HttpURLConnection) url.openConnection();
            if (url.getProtocol().toLowerCase().equals("https")) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                https.setHostnameVerifier(DO_NOT_VERIFY);
                //con = https;
            }

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);

            connection.setDoOutput(true);

            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            os = connection.getOutputStream();
            os.write(postData.getBytes());

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();

            } else {
                System.out.println("bad status code:" + connection.getResponseCode());
                Map<String, List<String>> map = connection.getHeaderFields();
                for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                    System.out.println("Key : " + entry.getKey() +
                            " ,Value : " + entry.getValue());
                }
                is = connection.getErrorStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

                    StringBuffer sbf = new StringBuffer();
                    String temp = null;
                    while ((temp = br.readLine()) != null) {
                        sbf.append(temp);
                        sbf.append("\r\n");
                    }
                    result = sbf.toString();
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();
        }
        return result;
    }


    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    private String extractValueFromJsonString(String jsonString, String key) {
        String regex = "\"" + key + "\":([^(,|\\}|\\])]*)";
        Matcher matcher = Pattern.compile(regex).matcher(jsonString);
        while (matcher.find()) {
            String val = matcher.group(1);
            val = val.trim();
            if (val.startsWith("\"")) {
                val = val.substring(1);
            }
            if (val.endsWith("\"")) {
                val = val.substring(0, val.length() - 1);
            }
            return val;
        }
        return null;

    }

    private String apiCall(String apiServer, String apiUri, File imageFile,String Appcode) {
        try {

            byte[] buffer = resizeAndReadImage(imageFile);
            if (buffer == null) {
                return null;
            }

            String imageBase64 = base64(buffer);
            String fileName = imageFile.getName();
            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
            String photoType = fileExt.replace(".", "");

            Map bodyJson = new HashMap();
            bodyJson.put("photo", imageBase64);
            bodyJson.put("type", photoType);
            bodyJson.put("face_required", 0); //可选，整形，检测是否必须带有人脸才进行抠图处理，0为检测，1为不检测，默认为0，设置这个参数后，可能导致人体背影无法抠图
            bodyJson.put("is_crop_content", 0); //可选，整形，是否值返回人像区域，其余透明区域，都被裁剪掉，0为否，1为是，默认为0
            String body = JSON.toJSONString(bodyJson);


            String accept = "application/json";
            String contentType = "application/octet-stream; charset=utf-8";

            Map<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", accept);
            headers.put("Authorization", "APPCODE "+Appcode);
            headers.put("Content-Type", contentType);


            String result = httpPost(apiServer + apiUri, headers, body);
            if (result == null) {
                System.out.println("httpPost call fail");
                return null;
            }

            System.out.println("result:" + result);
            return result;


        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    // 人像抠图返回透明背景
    public String mattingDetect(File imageFile, String Appcode) {
        System.out.println("智能人像分割（抠图）接口 人像抠图返回透明背景 ...");
        try {

            String result = apiCall(this.MattingAPIServer,
                    this.MattingAPIUri,
                    imageFile,
                    Appcode
            );
            if (result == null) {
                return null;
            }

            // 正确做法是使用json解析库，
            // 这里为了减少依赖，直接使用正则表达式解析
            String status = extractValueFromJsonString(result, "status");
            if (!status.equals("0")) {
                System.out.println("bad status:" + status);
                return null;
            }

            return JSON.parseObject(result).getJSONObject("data").getString("result");
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) {

        /*
         说明：
             此代码是一份调用接口调用示例，
             WXAIPhotoAPI也只是一份调用示例的class，
             并不是接口本身，
             请不要把WXAIPhotoAPI的参数，当成接口本身的参数
        */
        Matting api_test = new Matting();

        String imageFileName = "C:\\Users\\16479\\Pictures\\0d701ab747fa7fe84735b164a5323e9.jpg";
        String Appcode = "08cb5df6bd994865805ec5d7d537be29";

        File imageFile = new File(imageFileName);

        api_test.mattingDetect(imageFile, Appcode);

        return;
    }
}
