package com.yn.printer.service.modules.common.service.impl;

import cn.hutool.core.img.GraphicsUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.yn.printer.service.common.exception.YnError;
import com.yn.printer.service.common.exception.YnErrorException;
import com.yn.printer.service.modules.common.api.ali.idPhoto.AliIdPhotoApi;
import com.yn.printer.service.modules.common.api.ali.idPhoto.Matting;
import com.yn.printer.service.modules.common.api.ali.idPhoto.request.IdPhotoMakeRequest;
import com.yn.printer.service.modules.common.api.ali.idPhoto.response.IdPhotoMakeResponse;
import com.yn.printer.service.modules.common.constant.ColorEnum;
import com.yn.printer.service.modules.common.mqtt.MqttConfig;
import com.yn.printer.service.modules.common.mqtt.MqttSender;
//import com.yn.printer.service.modules.common.mqtt.dto.UpdatePush;
import com.yn.printer.service.modules.common.oss.OssConfig;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.util.PdfUtil;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.enums.DeviceStatus;
import com.yn.printer.service.modules.member.entity.PointsFile;
import com.yn.printer.service.modules.member.repository.PointsFileRepository;
import com.yn.printer.service.modules.meta.entity.MetaFile;
import com.yn.printer.service.modules.meta.enums.IdPhotoSize;
import com.yn.printer.service.modules.meta.repository.MetaFileRepository;
import com.yn.printer.service.modules.meta.repository.ThirdPartyVouchersRepository;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
import com.yn.printer.service.modules.operation.repository.DevicesListRepository;
import com.yn.printer.service.modules.operation.repository.TutorialsRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Value("${oss.s3.bucketName}")
    private String bucketName;
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

        try (FileInputStream fis = (FileInputStream) file.getInputStream();
             FileOutputStream fos = new FileOutputStream(dest);
             FileChannel inChannel = fis.getChannel();
             FileChannel outChannel = fos.getChannel()) {
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
            IdPhotoMakeResponse response = aliIdPhotoApi.make(request,
                    thirdPartyVouchersRepository.findAll().get(0).getAliIdPhotoAppCode());
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
            if (image.getHeight() < image.getWidth())
                ImgUtil.rotate(src, 90, src);
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
            if (image.getHeight() < image.getWidth())
                ImgUtil.rotate(src, 90, src);
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

        GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, newFileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);
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


}
