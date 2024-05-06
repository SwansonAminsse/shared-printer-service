package com.yn.printer.service.modules.common.controller;

import com.yn.printer.service.common.vo.ResponseVO;
import com.yn.printer.service.modules.common.constant.ColorEnum;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.meta.enums.IdPhotoSize;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Validated
@Api(value = "MemberController", tags = "公共端-文件")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    IFileService fileService;

    @ApiOperation(value = "文件-上传")
    @PostMapping(value = "/upload")
    public MetaFileVo upload(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
        return fileService.uploadFile(file);
    }
    @ApiOperation(value = "文件-更新")
    @PostMapping(value = "/update")
    public MetaFileVo update(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
        return fileService.updataFile(file);
    }

    @ApiOperation(value = "文件-上传-nio")
    @PostMapping(value = "/upload/nio")
    public MetaFileVo uploadNio(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
        return fileService.uploadFileNIO(file);
    }

    @ApiOperation(value = "文件-上传并转PDF")
    @PostMapping(value = "/upload2Pdf")
    public MetaFileVo upload2Pdf(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
        return fileService.upload2Pdf(file);
    }

    @ApiOperation(value = "文件-图片转PDF-身份证等证件照专用-转换前不做缩放处理")
    @PostMapping(value = "/image2Pdf")
    public MetaFileVo image2Pdf(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
        return fileService.image2Pdf(file);
    }

    @ApiOperation(value = "文件-多张图片转PDF-上传并转PDF")
    @PostMapping(value = "/images2Pdf")
    public MetaFileVo images2Pdf(@RequestPart("images") @ApiParam(value = "文件", required = true) List<MultipartFile> images) {
        return fileService.images2Pdf(images);
    }

    @ApiOperation(value = "文件-下载")
    @GetMapping(value = "/download")
    public void download(@RequestParam Long id, HttpServletResponse response) {
        fileService.downloadFile(id, response);
    }


    @ApiOperation(value = "文件-证件照切换底色")
    @PostMapping(value = "/changeIdPhotoBgc")
    public MetaFileVo changeIdPhotoBgc(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file,
                                       @RequestParam(required = false) @ApiParam(value = "颜色 CLEAR(透明),RED(红色),BLUE(蓝色),WHITE(白色);") ColorEnum colorEnum) {
        return fileService.changeIdPhotoBgc(file, colorEnum);
    }

    @ApiOperation(value = "文件-证件照排版转换")
    @PostMapping(value = "/changeIdPhotoLayout")
    public MetaFileVo changeIdPhotoLayout(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file,
                                          @RequestParam @ApiParam(value = "证件照尺寸 " +
                                                  "  M1(\"一寸\", 413, 295),\n" +
                                                  "  S1(\"小一寸\", 378, 260),\n" +
                                                  "  L1(\"大一寸\", 567, 390),\n" +
                                                  "  S2(\"小二寸\", 531, 413),\n" +
                                                  "  M2(\"二寸\", 579, 413),\n" +
                                                  "  M3(\"三寸\", 992, 650)") IdPhotoSize idPhotoSize) {
        return fileService.changeIdPhotoLayout(file, idPhotoSize);
    }

    @ApiOperation(value = "文件-证件照排版转换 + 切换底色")
    @PostMapping(value = "/changeIdPhotoLayoutAndChangeIdPhotoBgc")
    public MetaFileVo changeIdPhotoLayoutAndChangeIdPhotoBgc(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file,
                                                             @RequestParam @ApiParam(value = "证件照尺寸 " +
                                                                     "  M1(\"一寸\", 413, 295),\n" +
                                                                     "  S1(\"小一寸\", 378, 260),\n" +
                                                                     "  L1(\"大一寸\", 567, 390),\n" +
                                                                     "  S2(\"小二寸\", 531, 413),\n" +
                                                                     "  M2(\"二寸\", 579, 413),\n" +
                                                                     "  M3(\"三寸\", 992, 650)") IdPhotoSize idPhotoSize,
                                                             @RequestParam @ApiParam(value = "颜色 RED(红色),BLUE(蓝色),WHITE(白色);") ColorEnum colorEnum) {
        return fileService.changeIdPhotoLayoutAndChangeIdPhotoBgc(file, idPhotoSize, colorEnum);
    }

    @ApiOperation(value = "获取教程")
    @GetMapping("/getTutorialFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tutorialType", value = "Host或者Applet", dataType = "com.yn.printer.service.modules.operation.enums.TutorialTypes")
    })
    public MetaFileVo getTutorialFile(@RequestParam TutorialTypes tutorialType) {
        return fileService.getTutorialFile(tutorialType);
    }

    @ApiOperation(value = "获取预签名")
    @GetMapping(value = "/getURL")
    public MetaFileVo getURL(@RequestParam @ApiParam(value = "文件名", required = true) String fileType) {
        return fileService.getPresignedUrl(fileType);
    }

    @ApiOperation(value = "完成上传后下载")
    @GetMapping(value = "/toPDF")
    public MetaFileVo toPDF(@RequestParam @ApiParam(value = "预签名", required = true) String fileName) {
        return fileService.toPDF(fileName);
    }

}