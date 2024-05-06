package com.yn.printer.service.modules.common.controller.auth;

import com.yn.printer.service.modules.common.constant.ColorEnum;
import com.yn.printer.service.modules.common.service.IFileService;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Validated
@Api(value = "MemberController", tags = "公共端-文件")
@RestController
@RequestMapping("/file/auth")
public class FileAuthController {

    @Autowired
    IFileService fileService;

//    @ApiOperation(value = "文件-上传")
//    @PostMapping(value = "/upload")
//    public MetaFileVo upload(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
//        return fileService.uploadFile(file);
//    }
//
//    @ApiOperation(value = "文件-上传并转PDF")
//    @PostMapping(value = "/upload2Pdf")
//    public MetaFileVo upload2Pdf(@RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file) {
//        return fileService.upload2Pdf(file);
//    }
//
//    @ApiOperation(value = "文件-下载")
//    @GetMapping(value = "/download")
//    public void download(@RequestParam("id") @ApiParam(value = "文件id", required = true) Long id) {
//        fileService.downloadFile(id);
//    }
//
//    @ApiOperation(value = "文件-证件照切换底色")
//    @PostMapping(value = "/changeIdPhotoBgc")
//    public MetaFileVo changeIdPhotoBgc(
//            @RequestPart("file") @ApiParam(value = "文件", required = true) MultipartFile file,
//            @ApiParam(value = "颜色 RED(红色),BLUE(蓝色),WHITE(白色);", required = true) ColorEnum colorEnum) {
//        return fileService.changeIdPhotoBgc(file, colorEnum);
//    }

}
