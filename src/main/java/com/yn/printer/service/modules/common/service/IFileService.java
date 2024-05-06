package com.yn.printer.service.modules.common.service;

import com.yn.printer.service.modules.common.constant.ColorEnum;
import com.yn.printer.service.modules.common.vo.MetaFileVo;
import com.yn.printer.service.modules.meta.entity.MetaFile;
import com.yn.printer.service.modules.meta.enums.IdPhotoSize;
import com.yn.printer.service.modules.operation.enums.TutorialTypes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

public interface IFileService {

    MetaFile file2MetaFile(File desc);

    File metaFile2File(MetaFile metaFile);

    MetaFileVo uploadFile(MultipartFile file);

    MetaFileVo updataFile(MultipartFile file);

//    Boolean Update(MultipartFile file, String log);

    MetaFileVo uploadFileNIO(MultipartFile file);

    String findPreviewUrl(MetaFile metaFile);

    void downloadFile(Long id, HttpServletResponse response);

    MetaFileVo upload2Pdf(MultipartFile file);

    MetaFileVo image2Pdf(MultipartFile file);

    MetaFileVo images2Pdf(List<MultipartFile> images);

    MetaFileVo changeIdPhotoBgc(MultipartFile file, ColorEnum colorEnum);

    MetaFileVo changeIdPhotoLayout(MultipartFile file, IdPhotoSize idPhotoSize);

    MetaFileVo changeIdPhotoLayoutAndChangeIdPhotoBgc(MultipartFile file, IdPhotoSize idPhotoSize, ColorEnum colorEnum);

    MetaFileVo metaFile2Vo(MetaFile metaFile);

    MetaFileVo getTutorialFile(TutorialTypes tutorialType);

    MetaFileVo getPresignedUrl(String fileType);

    MetaFileVo toPDF(String fileName);
}
