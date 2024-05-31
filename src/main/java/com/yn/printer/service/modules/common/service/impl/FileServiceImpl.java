package com.yn.printer.service.modules.common.service.impl;

import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.modules.channel.entity.ApiRequest;
import com.yn.printer.service.modules.common.api.ali.idPhoto.AliIdPhotoApi;
import com.yn.printer.service.modules.common.api.ali.idPhoto.Matting;
import com.yn.printer.service.modules.common.api.ali.idPhoto.request.IdPhotoMakeRequest;
import com.yn.printer.service.modules.common.api.ali.idPhoto.response.IdPhotoMakeResponse;
import com.yn.printer.service.modules.common.constant.ColorEnum;
import com.yn.printer.service.modules.common.mqtt.MqttConfig;
import com.yn.printer.service.modules.common.mqtt.MqttSender;
import com.yn.printer.service.modules.common.oss.OssConfig;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.util.Base64Util;
import com.yn.printer.service.modules.common.util.PdfUtil;
import com.yn.printer.service.modules.common.vo.ApiResponse;
import com.yn.printer.service.modules.common.vo.CallbackResult;
import com.yn.printer.service.modules.common.vo.IDcardRecoVO;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.member.repository.PointsFileRepository;
import com.yn.printer.service.modules.meta.entity.MetaFile;
import com.yn.printer.service.modules.meta.enums.IdPhotoSize;
import com.yn.printer.service.modules.meta.repository.MetaFileRepository;
import com.yn.printer.service.modules.meta.repository.ThirdPartyVouchersRepository;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.repository.TutorialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.List;

/**
 * @author : Jonas Chan
 * @since : 2023/12/28 22:30
 */
@Service
@Slf4j
public class FileServiceImpl implements IFileService {

    @Value("${file.basePath}")
    String basePath;

    @Value("${file.previewPath}")
    String previewPath;
    @Value("${file.ossPath}")
    String ossPath;
    @Value("${file.ossPath1}")
    String ossPath1;
    @Autowired
    MetaFileRepository metaFileRepository;
    @Autowired
    TutorialsRepository tutorialsRepository;
    @Autowired
    AliIdPhotoApi aliIdPhotoApi;
    @Autowired
    ThirdPartyVouchersRepository thirdPartyVouchersRepository;
    @Autowired
    PointsFileRepository pointsFileRepository;
    @Autowired
    OssConfig s3;
    @Autowired
    Matting matting;
    @Autowired
    DevicesListRepository devicesListRepository;
    @Autowired
    MqttSender mqttSender;
    @Autowired
    MqttConfig mqttConfig;
    @Value("${oss.s3.bucketName}")
    private String bucketName;
    @Value("${file.username}")
    String username;

    @Override
    public MetaFileVo uploadFile(MultipartFile file) {

        // 处理苹果图片
        String last = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (last.equalsIgnoreCase("HEIC")) last = "jpg";

        File dest = new File(FileUtil.mkdir(basePath), System.nanoTime() + "." + last);

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_100006);
        }

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, dest.getName(), dest);
        log.info("结束上传转码文件");

        return metaFile2Vo(this.file2MetaFile(dest));
    }

    @Override
    public MetaFileVo updataFile(MultipartFile file) {
        String key = file.getOriginalFilename();

        File dest = new File(FileUtil.mkdir(basePath), System.nanoTime() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), "."));

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_100006);
        }
        try {
            pointsFileRepository.deleteAll();
        } catch (Exception e) {
            log.info("删除积分档案失败");

        }

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, key, dest);
        log.info("结束上传转码文件");
        System.out.println(s3.s3Client1().getUrl(bucketName, key));

        return null;
    }

//    @Override
//    public Boolean Update(MultipartFile file, String log) {
//        UpdatePush updatePush = new UpdatePush();
//        String key = file.getOriginalFilename();
//        File dest = new File(FileUtil.mkdir(basePath), System.nanoTime() + "." + StringUtils.substringAfterLast(file.getOriginalFilename(), "."));
//        try {
//            file.transferTo(dest);
//            s3.s3Client1().putObject(bucketName, key, dest);
//            String url = s3.s3Client1().getUrl(bucketName, key).toString();
//            updatePush.setUrl(url);
//            updatePush.setLog(log);
//            List<DevicesList> devices = devicesListRepository.findByStatus(DeviceStatus.NOT_ACTIVE);
//            List<String> deviceCodes = devices.stream()
//                    .map(DevicesList::getCode)
//                    .collect(Collectors.toList());
//            for (String deviceCode : deviceCodes) {
//                this.mqttPush(deviceCode, JSON.toJSONString(updatePush));
//            }
//            System.out.println(url);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new YnErrorException(YnError.YN_100006);
//        }
//    }

//    public void mqttPush(String code, String message) {
//        String topic = mqttConfig.topicSendDevice + code;
//        mqttSender.send(topic, message);
//        log.info("推送MQTT消息 主题: {}, 内容: {}", topic, message);
//    }

    /**
     * 文件上传方法 使用 nio
     */
    public MetaFileVo uploadFileNIO(MultipartFile file) {
        // 处理苹果图片
        String last = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        if (last.equalsIgnoreCase("HEIC")) last = "jpg";
        //获取文件名
        File dest = new File(FileUtil.mkdir(basePath), System.nanoTime() + "." + last);

        try (FileInputStream fis = (FileInputStream) file.getInputStream(); FileOutputStream fos = new FileOutputStream(dest); FileChannel inChannel = fis.getChannel(); FileChannel outChannel = fos.getChannel()) {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_100006);
        }

        return metaFile2Vo(this.file2MetaFile(dest));
    }

    @Override
    public String findPreviewUrl(MetaFile metaFile) {
        return previewPath + metaFile.getFilePath();
    }

    @Override
    public void downloadFile(Long id, HttpServletResponse response) {

        MetaFile metaFile = metaFileRepository.findById(id).orElse(null);
        if (metaFile == null) throw new YnErrorException(YnError.YN_700001);

        try (FileInputStream is = new FileInputStream(basePath + metaFile.getFilePath())) {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            // 设置强制下载不打开
            response.setContentType("application/octet-stream");
            // 设置文件名
            response.addHeader("Content-Disposition", "attachment;fileName=" + metaFile.getFileName());
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MetaFileVo upload2Pdf(MultipartFile file) {

        MetaFileVo metaFileVo = this.uploadFile(file);

        File src = new File(basePath + metaFileVo.getFilePath());
        File dest = PdfUtil.fileToPdf(src, null);

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, dest.getName(), dest);
        log.info("结束上传转码文件");

        return metaFile2Vo(this.file2MetaFile(dest));
    }

    @Override
    public MetaFileVo image2Pdf(MultipartFile file) {
        MetaFileVo metaFileVo = this.uploadFile(file);

        File src = new File(basePath + metaFileVo.getFilePath());
        File dest = PdfUtil.image2Pdf(src, null);

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, dest.getName(), dest);
        log.info("结束上传转码文件");

        return metaFile2Vo(this.file2MetaFile(dest));
    }

    @Override
    public MetaFileVo images2Pdf(List<MultipartFile> images) {

        File dir = FileUtil.mkdir(basePath + System.currentTimeMillis() + RandomUtil.randomString(20));

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            FileUtil.copy(basePath + this.uploadFile(image).getFilePath(), dir.getPath() + "/" + i + ".png", true);
        }

        return metaFile2Vo(this.file2MetaFile(PdfUtil.fileToPdf(dir, null)));
    }

    @Override
    public MetaFile file2MetaFile(File desc) {
//        PutObjectRequest request = new PutObjectRequest(bucketName, desc.getName(), desc);
//        s3.s3Client().putObject(request);
        MetaFile metaFile = new MetaFile();
        metaFile.setFileName(desc.getName());
        metaFile.setFilePath(desc.getName());
        metaFile.setFileSize(FileUtil.size(desc));
        metaFile.setFileType(FileTypeUtil.getType(desc));
        metaFileRepository.save(metaFile);
        return metaFile;
    }

    @Override
    public File metaFile2File(MetaFile metaFile) {
        return new File(basePath + metaFile.getFilePath());
    }

    @Override
    public MetaFileVo changeIdPhotoBgc(MultipartFile file, ColorEnum colorEnum) {
        MetaFileVo metaFileVo = this.uploadFile(file);
        File src = new File(basePath + metaFileVo.getFilePath());

        String url = matting.mattingDetect(src, thirdPartyVouchersRepository.findAll().get(0).getAliyunMattingAppCode());
        if (url == null) throw new YnErrorException(YnError.YN_000000);

        HttpUtil.downloadFile(url, src);

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, src.getName(), src);
        log.info("结束上传转码文件");

        metaFileVo.setPreviewPath(previewPath + metaFileVo.getFilePath());

        return metaFileVo;
    }

    @Override
    public MetaFileVo changeIdPhotoLayout(MultipartFile file, IdPhotoSize idPhotoSize) {
        MetaFileVo metaFileVo = this.uploadFile(file);

        File src = new File(basePath + metaFileVo.getFilePath());

        // 图片预处理, 图片排版
        try {
            idPhotoLayout(src, src);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_700002);
        }

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, src.getName(), src);
        log.info("结束上传转码文件");

        return metaFileVo;
    }

    @Override
    public MetaFileVo changeIdPhotoLayoutAndChangeIdPhotoBgc(MultipartFile file, IdPhotoSize idPhotoSize, ColorEnum colorEnum) {

        MetaFileVo metaFileVo = this.uploadFile(file);
        File src = new File(basePath + metaFileVo.getFilePath());

        // 图片预处理, 换底色+裁剪
        try {
            IdPhotoMakeRequest request = new IdPhotoMakeRequest();
            request.setType(src.getPath().substring(src.getPath().lastIndexOf(".") + 1));
            request.setPhoto(Base64.getEncoder().encodeToString(FileUtil.readBytes(src)));
            request.setSpec(idPhotoSize.getSpec().toString());
            request.setBk(colorEnum.name().toLowerCase());
            IdPhotoMakeResponse response = aliIdPhotoApi.make(request, thirdPartyVouchersRepository.findAll().get(0).getAliIdPhotoAppCode());
            HttpUtil.downloadFile(response.getResult(), src);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_700002);
        }

        // 图片预处理, 图片排版
        try {
            idPhotoLayout(src, src);
        } catch (Exception e) {
            e.printStackTrace();
            throw new YnErrorException(YnError.YN_700003);
        }

        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, src.getName(), src);
        log.info("结束上传转码文件");

        return metaFileVo;
    }

    @Override
    public MetaFileVo metaFile2Vo(MetaFile metaFile) {

        File src = metaFile2File(metaFile);
        MetaFileVo metaFileVo = new MetaFileVo();

        BeanUtils.copyProperties(metaFile, metaFileVo);
        metaFileVo.setPreviewPath(ossPath + metaFile.getFilePath());
        metaFileVo.setDownloadPath(ossPath + metaFile.getFilePath());
        metaFileVo.setOuterChainPath(ossPath + metaFile.getFilePath());
        // 返回PDF页数
        int pages = "pdf".equals(metaFile.getFileType()) ? PdfUtil.getNumberOfPages(src) : 1;
        metaFileVo.setPdfPageSize(pages);
        return metaFileVo;
    }

    @Override
    public MetaFileVo getTutorialFile(TutorialTypes tutorialType) {
        return metaFile2Vo(tutorialsRepository.findByTutorialType(tutorialType).getContent());
    }

    private void idPhotoLayout(File src, File dest) {
        // 图片预处理, 图片排版
        try (FileInputStream fisVertical = new FileInputStream(src)) {
            Image image = Image.getInstance(src.getPath());
            // 如果高比宽小, 则图片 90度 旋转, 使其竖排
            if (image.getHeight() < image.getWidth()) ImgUtil.rotate(src, 90, src);
            // 复制证件照
            int imageType = 1;
            // 排版留白间隙 px
            int gap = 10;
            // 竖排照
            BufferedImage vertical = ImageIO.read(fisVertical);
            // 标准 6寸相纸尺寸: 1800 * 1200
            final BufferedImage board = new BufferedImage(1800, 1200, imageType);
            final Graphics2D bGr = GraphicsUtil.createGraphics(board, Color.WHITE);
            // 排版写入
            int x = gap, y = gap;
            while (true) {
                // 判断是否需要换行
                if (x + vertical.getWidth() + gap > board.getWidth()) {
                    x = gap;
                    // 计算出下一个证件照写入的 Y 轴
                    y += vertical.getHeight() + gap;
                    continue;
                }
                // 判断是否结束写入
                if (y + vertical.getHeight() + gap > board.getHeight()) {
                    break;
                }
                // 证件照写入画板
                bGr.drawImage(vertical, x, y, null);
                // 计算出下一个证件照写入的 X 轴
                x += vertical.getWidth() + gap;
            }
            bGr.dispose();
            ImageIO.write(board, src.getName().substring(src.getName().lastIndexOf(".") + 1), dest);
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
    }

    private void idPhotoLayoutGreedy(File src, File dest) {
        // 图片预处理, 图片排版
        try (FileInputStream fisVertical = new FileInputStream(src)) {
            Image image = Image.getInstance(src.getPath());
            // 如果高比宽小, 则图片 90度 旋转, 使其竖排
            if (image.getHeight() < image.getWidth()) ImgUtil.rotate(src, 90, src);
            // 复制证件照
            int imageType = 1;
            // 排版留白间隙 px
            int gap = 10;
            // 竖排照
            BufferedImage vertical = ImageIO.read(fisVertical);
            // 标准 6寸相纸尺寸: 1800 * 1200
            final BufferedImage board = new BufferedImage(1800, 1200, imageType);
            final Graphics2D bGr = GraphicsUtil.createGraphics(board, Color.WHITE);
            // 排版写入
            int x = gap, y = gap;
            while (true) {
                // 判断是否需要换行
                if (x + vertical.getWidth() + gap > board.getWidth()) {
                    x = gap;
                    // 计算出下一个证件照写入的 Y 轴
                    y += vertical.getHeight() + gap;
                    continue;
                }
                // 判断是否结束写入
                if (y + vertical.getHeight() + gap > board.getHeight()) {
                    break;
                }
                // 证件照写入画板
                bGr.drawImage(vertical, x, y, null);
                // 计算出下一个证件照写入的 X 轴
                x += vertical.getWidth() + gap;
            }
            bGr.dispose();
            ImageIO.write(board, src.getName().substring(src.getName().lastIndexOf(".") + 1), dest);
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MetaFileVo getPresignedUrl(String fileType) {
        Date expiration = new Date();
        long expTimeMillis = System.currentTimeMillis() + 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        String newFileName = getNewFileName(fileType);

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, newFileName).withMethod(HttpMethod.PUT).withExpiration(expiration);
        MetaFileVo metaFileVo = new MetaFileVo();
        metaFileVo.setFileName(newFileName);
        metaFileVo.setDownloadPath(s3.s3Client().generatePresignedUrl(urlRequest).toString());
        return metaFileVo;
    }

    private String getNewFileName(String fileType) {
        long timestamp = System.currentTimeMillis();
        return timestamp + "." + fileType;
    }

    @Override
    public MetaFileVo toPDF(String fileName) {
        String downloadUrl = ossPath1 + fileName;
        File src = new File(basePath + fileName);
        log.info("开始下载源文件 = {}", downloadUrl);
        HttpUtil.downloadFile(downloadUrl, src);
        log.info("结束下载源文件");
        log.info("开始源文件转码");
        File dest = PdfUtil.fileToPdf(src, null);
        log.info("结束源文件转码");
        log.info("开始上传转码文件");
        s3.s3Client1().putObject(bucketName, dest.getName(), dest);
        log.info("结束上传转码文件");
        return metaFile2Vo(file2MetaFile(dest));
    }

    private void imgRiskControl(ApiRequest request) {
        String url = "http://api-img-bj.fengkongcloud.com/image/v4";
        try {
            // 构建请求体
            JSONObject json = new JSONObject();
            json.put("accessKey", request.getAccessKey());
            json.put("appId", request.getAppId());
            JSONObject data = new JSONObject();
            data.put("img", request.getImg());
            data.put("tokenId", request.getTokenId());
            json.put("data", data);
            json.put("eventId", request.getEventId());
            json.put("type", request.getType());
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 处理响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            // 解析响应
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println(jsonResponse);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void docRiskControl(ApiRequest request) {
        String url = "http://api-article-bj.fengkongcloud.com/v1/saas/anti_fraud/article";
        try {
            // 构建请求体
            JSONObject json = new JSONObject();
            json.put("accessKey", request.getAccessKey());
            json.put("type", request.getType());
            json.put("imgType", request.getImgType());
            json.put("txtType", request.getTxtType());
            json.put("callback", request.getCallback());
            JSONObject data = new JSONObject();
            data.put("fileFormat", request.getFileFormat());
            data.put("tokenId", request.getTokenId());
            data.put("contents", request.getContents());
            json.put("data", data);
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 处理响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            // 解析响应
            JSONObject jsonResponse = new JSONObject(response.toString());
            System.out.println(jsonResponse);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void callApi(ApiRequest request) {
        if ("img".equalsIgnoreCase(request.getInterType())) {
            imgRiskControl(request);
        } else if ("document".equalsIgnoreCase(request.getInterType())) {
            docRiskControl(request);
        } else {
            throw new IllegalArgumentException("Invalid interType: " + request.getInterType());
        }
    }

    @Override
    public ApiResponse handleCallBack(String requestId, String accessKey) {
        String url = "http://api-article-bj.fengkongcloud.com/v1/saas/anti_fraud/article/query";
        try {
            // 构建请求体
            JSONObject json = new JSONObject();
            json.put("accessKey", accessKey);
            JSONArray requestIds = new JSONArray();
            requestIds.put(requestId);
            json.put("requestIds", requestIds);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            // 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            // 处理响应
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            // 解析响应
            JSONObject jsonResponse = new JSONObject(response.toString());
            ObjectMapper mapper = new ObjectMapper();
            ApiResponse apiResponse = mapper.readValue(jsonResponse.toString(), ApiResponse.class);
            System.out.println(apiResponse.toString());
            return apiResponse;
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public IDcardRecoVO IDcardReco(MultipartFile file, String typeId) {
        String url = "http://103.139.212.226:8888/cxfServerX/doAllCardFileRecon";

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 构建请求体
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("typeId", typeId);
        ByteArrayResource fileAsResource = null;
        try {
            fileAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        body.add("file", fileAsResource);

        // 发送请求
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        IDcardRecoVO iDcardRecoVO = restTemplate.postForObject(url, requestEntity, IDcardRecoVO.class);
        System.out.println(iDcardRecoVO.toString());
        return iDcardRecoVO;
    }

    //对两张照片进行解码,调用previewImages方法完成身份证预览
    public BufferedImage handleIDcard(IDcardRecoVO frontIDcardRecoVO, IDcardRecoVO backIDcardRecoVO,
                                      String frontoutputFilePath, String backoutputFilePath, HttpServletResponse response) {
        try {
            String base64ImageString = null;
            List<IDcardRecoVO.Item> items = frontIDcardRecoVO.getData().getCardsinfo().getCard().getItem();
            for (IDcardRecoVO.Item item : items) {
                if ("处理后的图片".equals(item.getDesc())) {
                    String content = item.getContent();
                    base64ImageString = content;
                }
            }
            Base64Util base64Util = new Base64Util();

            base64Util.decodeImage(base64ImageString, frontoutputFilePath);
            List<IDcardRecoVO.Item> items1 = backIDcardRecoVO.getData().getCardsinfo().getCard().getItem();
            for (IDcardRecoVO.Item item : items1) {
                if ("处理后的图片".equals(item.getDesc())) {
                    String content = item.getContent();
                    base64ImageString = content;
                }
            }
            base64Util.decodeImage(base64ImageString, backoutputFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BufferedImage bufferedImage = previewImages(frontoutputFilePath, backoutputFilePath, response);
        return bufferedImage;
    }

    private BufferedImage previewImages(String frontOutPutFilePath, String backOutPutFilePath, HttpServletResponse response) {
        try {
            File image1 = new File(basePath + frontOutPutFilePath);
            File image2 = new File(basePath + backOutPutFilePath);

            if (!image1.exists() || !image2.exists()) {
                throw new YnErrorException(YnError.YN_700001);
            }

            BufferedImage combinedImage = mergeImages(image1, image2);
            response.setContentType("image/jpg");
            ImageIO.write(combinedImage, "jpg", response.getOutputStream());
            return combinedImage;
        } catch (IOException e) {
            throw new YnErrorException(YnError.YN_700002);
        }
    }

    //将身份证正反面放入A4纸中预览
    private BufferedImage mergeImages(File image1, File image2) throws IOException {
        // 从文件中读取图像
        BufferedImage img1 = ImageIO.read(image1);
        BufferedImage img2 = ImageIO.read(image2);

        int A4_WIDTH = 2480; // A4纸宽度像素 (300 dpi)
        int A4_HEIGHT = 3508; // A4纸高度像素 (300 dpi)
        int MARGIN = 50; // 图片与边界的距离
        int SPACING = 20; // 图片之间的距离

        // 创建一个新的图像，用于存放合并后的图像
        BufferedImage combined = new BufferedImage(A4_WIDTH, A4_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combined.createGraphics();

        // 填充背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, A4_WIDTH, A4_HEIGHT);

        // 计算图片的最大宽度
        int maxWidth = A4_WIDTH - 2 * MARGIN;
        // 计算图片1的高度
        int img1Height = img1.getHeight() * maxWidth / img1.getWidth();
        // 计算图片2的高度
        int img2Height = img2.getHeight() * maxWidth / img2.getWidth();

        // 计算图片1的y坐标
        int yPositionImg1 = MARGIN;
        // 计算图片2的y坐标
        int yPositionImg2 = yPositionImg1 + img1Height + SPACING;

        // 将图片1绘制到新图像上
        g.drawImage(img1, MARGIN, yPositionImg1, maxWidth, img1Height, null);
        // 将图片2绘制到新图像上
        g.drawImage(img2, MARGIN, yPositionImg2, maxWidth, img2Height, null);

        // 释放资源
        g.dispose();
        // 返回合并后的图像
        return combined;
    }

}
