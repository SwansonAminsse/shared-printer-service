package com.yn.printer.service.modules.common.util;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.aspose.cells.PdfSaveOptions;
import com.aspose.cells.Workbook;
import com.aspose.slides.Presentation;
import com.aspose.words.FontSettings;
import com.aspose.words.SaveFormat;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * pdf 相关工具
 *
 * @author : Jonas Chan
 * @since : 2023/12/26 22:02
 */
@Component
public class PdfUtil {

    private static final String PDF = ".pdf";
    private static final String IMAGE_JPG = ".jpg";
    private static final String IMAGE_PNG = ".png";
    private static final String WORD_DOC = ".doc";
    private static final String WORD_DOCX = ".docx";
    private static final String EXCEL_XLS = ".xls";
    private static final String EXCEL_XLSX = ".xlsx";
    private static final String PPT = ".ppt";
    private static final String PPTX = ".pptx";
    private static final String ZIP = ".zip";

    // 指定默认打印页的尺寸
    private static final Rectangle DEFAULT_RECTANGLE = PageSize.A4;
    // 图片默认放大缩小百分比 %
    private static final int DEFAULT_IMAGE_PERCENT = 100;
    // 图片转pdf后最小留白外边距 px
    private static final int DEFAULT_IMAGE_MIN_MARGIN = 10;

    @Value("${spring.profiles.active}")
    public void setActive(String active) {
        // 字体设置, 生产环境使用
        if ("prod".equals(active))
            FontSettings.setFontsFolders(new String[]{"/usr/share/fonts", "/usr/share/fonts/chinese"}, true);
    }

    /**
     * 获取 pdf文件 页数
     *
     * @param file pdf文件
     * @return
     */
    public static int getNumberOfPages(File file) {
        PDDocument doc = null;
        int numberOfPages = -1;
        try {
            doc = PDDocument.load(file);
            numberOfPages = doc.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (doc != null) {
                try {
                    doc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return numberOfPages;
    }

    /**
     * file 转 pdf
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File fileToPdf(File src, File dest) {

        // 多图合成PDF
        if (src.isDirectory()) return images2Pdf(src, new File(src.getPath() + ".pdf"));

        // 源文件全路径
        String srcPath = src.getPath();
        // 获取源文件后缀名
        String ext = srcPath.substring(srcPath.lastIndexOf('.'));

        // 未指定转码文件生成路径时, 自动生成与源文件同名的转码文件
        if (dest == null) {
            String destPath = srcPath.substring(0, srcPath.lastIndexOf('.')) + ".pdf";
            dest = new File(destPath);
        }

        switch (ext) {
            case PDF:
                return src;
            case IMAGE_JPG:
            case IMAGE_PNG:
                return imageScale2Pdf(src, dest);
            case WORD_DOC:
            case WORD_DOCX:
                return wordToPdf(src, dest);
            case EXCEL_XLS:
            case EXCEL_XLSX:
                return excelToPdf(src, dest);
            case PPT:
            case PPTX:
                return pptToPdf(src, dest);
            case ZIP:
                return zipToPdf(src, dest);
            default:
                throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        }
    }

    /**
     * 图片缩放转pdf
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File imageScale2Pdf(File src, File dest) {

        // 创建一个文档对象, 左右自动居中, 上下默认对齐顶部
        // 最少外边距: 这里会设置一个顶部左部外边距, 外边距取缩小比例后上下留白大小的一半, 使其顶部留白
        Document doc = null;
        FileOutputStream os = null;
        try {
            // 获取图片实例
            Image image = Image.getInstance(src.getPath());
            // 如果长比宽小, 则图片 90度 旋转, 使其竖排
            if (image.getHeight() < image.getWidth()) {
                ImgUtil.rotate(src, 90, src);
                // 重新获取实例
                image = Image.getInstance(src.getPath());
            }
            // 等比例放大或缩小图片, 取长宽缩放比例更小值, 调整至 A4纸 大小的 defaultImagePercent %
            int percent = Math.min(
                    Math.round((DEFAULT_RECTANGLE.getWidth() - DEFAULT_IMAGE_MIN_MARGIN * 2) / image.getWidth() * DEFAULT_IMAGE_PERCENT),
                    Math.round((DEFAULT_RECTANGLE.getHeight() - DEFAULT_IMAGE_MIN_MARGIN * 2) / image.getHeight() * DEFAULT_IMAGE_PERCENT)
            );
            image.scalePercent(percent);

            // 缩放后图片宽高
            float afterPercentWidth = image.getWidth() * percent / 100;
            float afterPercentHeight = image.getHeight() * percent / 100;

            // 写入pdf
            float marginLeft = (DEFAULT_RECTANGLE.getWidth() - afterPercentWidth) / 2;
            float marginTop = (DEFAULT_RECTANGLE.getHeight() - afterPercentHeight) / 2;
            doc = new Document(DEFAULT_RECTANGLE, marginLeft, 0, marginTop, 0);
            os = new FileOutputStream(dest);
            PdfWriter.getInstance(doc, os);
            doc.open();
            doc.newPage();
            doc.add(image);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        } finally {
            // 这里要先关闭doc流, 再关闭文件流
            if (doc != null)
                doc.close();
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return dest;
    }

    /**
     * 图片直接转pdf
     * @param src
     * @param dest
     * @return
     */
    public static File image2Pdf(File src, File dest) {

        // 源文件全路径
        String srcPath = src.getPath();

        // 未指定转码文件生成路径时, 自动生成与源文件同名的转码文件
        if (dest == null) {
            String destPath = srcPath.substring(0, srcPath.lastIndexOf('.')) + ".pdf";
            dest = new File(destPath);
        }

        // 创建一个文档对象, 左右自动居中, 上下默认对齐顶部
        // 最少外边距: 这里会设置一个顶部左部外边距, 外边距取缩小比例后上下留白大小的一半, 使其顶部留白
        Document doc = null;
        FileOutputStream os = null;
        try {
            // 获取图片实例
            Image image = Image.getInstance(src.getPath());

            // 写入pdf
            float marginLeft = (DEFAULT_RECTANGLE.getWidth() - image.getWidth()) / 2;
            float marginTop = (DEFAULT_RECTANGLE.getHeight() - image.getHeight()) / 2;
            doc = new Document(DEFAULT_RECTANGLE, marginLeft, 0, marginTop, 0);
            os = new FileOutputStream(dest);
            PdfWriter.getInstance(doc, os);
            doc.open();
            doc.newPage();
            doc.add(image);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        } finally {
            // 这里要先关闭doc流, 再关闭文件流
            if (doc != null)
                doc.close();
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return dest;
    }

    /**
     * 图片排版+转pdf
     * 容器: 6寸相纸
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File imageLayout2Pdf(File src, File dest, Integer defaultImagePercent) {

        defaultImagePercent = defaultImagePercent == null || defaultImagePercent > 100 || defaultImagePercent < 0
                ? DEFAULT_IMAGE_PERCENT : defaultImagePercent;

        // 创建一个文档对象, 左右自动居中, 上下默认对齐顶部
        // 这里会设置一个顶部外边距, 顶部外边距取缩小比例后上下留白大小的一半, 使其顶部留白
        int marginTop = Math.round(PageSize.A6.getHeight() * (100 - defaultImagePercent) / 200);
        Document doc = new Document(PageSize.A6, 0, 0, marginTop, 0);
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(dest);
            PdfWriter.getInstance(doc, os);
            doc.open();
            doc.newPage();

            // 获取图片实例
            Image image = Image.getInstance(src.getPath());
            image.setAlignment(Image.ALIGN_CENTER);
            // 等比例放大或缩小图片, 以宽或高的最大值调整, 调整至 A6纸 大小的 90%
            int percent = image.getHeight() > image.getWidth()
                    ? Math.round(PageSize.A6.getHeight() / image.getHeight() * defaultImagePercent)
                    : Math.round(PageSize.A6.getWidth() / image.getWidth() * defaultImagePercent);
            image.scalePercent(percent);

            doc.add(image);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        } finally {
            // 这里要先关闭doc流, 再关闭文件流
            doc.close();
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        return dest;
    }

    /**
     * word 转 pdf
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File wordToPdf(File src, File dest) {

        try (FileOutputStream fo = new FileOutputStream(dest)) {
            // 此处处理乱码和小方块
            // 如果在本地运行,此处报错,请注释这个这是字体,主要是为了解决linux环境下面运行jar时找不到中文字体的问题
            // 指定文件库内容路径
            new com.aspose.words.Document(src.getPath()).save(fo, SaveFormat.PDF);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        }

        return dest;
    }

    /**
     * excel 转 pdf
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File excelToPdf(File src, File dest) {
        try {
            // 读取Excel文件
            FileInputStream fis = new FileInputStream(src);
            org.apache.poi.ss.usermodel.Workbook utf8Workbook = WorkbookFactory.create(new FileInputStream(src));
            FileOutputStream utf8Fos = new FileOutputStream(src);
            utf8Workbook.write(utf8Fos);
            utf8Fos.close();

            // 将Excel文件转换为PDF
            try (FileOutputStream fos = new FileOutputStream(dest)) {
                new Workbook(src.getPath()).save(fos, new PdfSaveOptions());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
            }
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("转换Excel文件为PDF时出现异常");
        }

    }

    /**
     * ppt 转 pdf
     *
     * @param src  源文件
     * @param dest 转码文件, 不传值则生成到同目录下
     * @return dest
     */
    public static File pptToPdf(File src, File dest) {

        try (FileInputStream fi = new FileInputStream(src);
             FileOutputStream fo = new FileOutputStream(dest)) {
            Presentation pres = new Presentation(fi);
            pres.save(fo, com.aspose.slides.SaveFormat.Pdf);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        }

        return dest;
    }

    /**
     * 压缩包转PDF 压缩包内文件为图片
     *
     * @param src
     * @param dest
     * @return
     */
    public static File zipToPdf(File src, File dest) {
        // 文件解压后存放目录
        File dir = FileUtil.mkdir(src.getPath().substring(0, src.getPath().lastIndexOf(".")));
        // 文件解压
        ZipUtil.unzip(src, dir);

        images2Pdf(dir, dest);

        // 删除解压后的文件
        FileUtil.del(dir);

        return dest;
    }

    /**
     * 多图转pdf
     *
     * @param src  图片存放目录
     * @param dest pdf文件
     * @return
     */
    public static File images2Pdf(File src, File dest) {

        Document document = null;
        FileOutputStream fo = null;

        try {
            fo = new FileOutputStream(dest);
            document = new Document();
            PdfWriter.getInstance(document, fo);
            document.open();

            for (File item : FileUtil.ls(src.getPath())) {
                Image image = Image.getInstance(item.getPath());
                image.setAlignment(Image.ALIGN_CENTER);
                // 等比例放大或缩小图片, 以宽或高的最大值调整, 调整至 A4纸 大小的 90%
                int percent = image.getHeight() > image.getWidth()
                        ? Math.round(DEFAULT_RECTANGLE.getHeight() / image.getHeight() * 100)
                        : Math.round(DEFAULT_RECTANGLE.getWidth() / image.getWidth() * 100);
                image.scalePercent(percent);

                document.add(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("文件[%S]不支持转换为PDF", src.getPath()));
        } finally {
            if (document != null) {
                document.close();
            }
            if (fo != null) {
                try {
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return dest;
    }

    static {
        // 验证aspose.word组件是否授权：无授权的文件有水印标记
        try (InputStream cells = PdfUtil.class.getClassLoader().getResourceAsStream("aspose-cells-license.xml");
             InputStream word = PdfUtil.class.getClassLoader().getResourceAsStream("aspose-word-license.xml");
             InputStream slides = PdfUtil.class.getClassLoader().getResourceAsStream("aspose-slides-license.xml")) {
            new com.aspose.cells.License().setLicense(cells);
            new com.aspose.words.License().setLicense(word);
            new com.aspose.slides.License().setLicense(slides);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

