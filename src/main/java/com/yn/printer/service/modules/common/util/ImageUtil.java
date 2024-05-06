package com.yn.printer.service.modules.common.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtil {

    public static void main(String[] args) {
        downImg();
    }

    public static void downImg() {

        File src = new File("C:\\Users\\16479\\Desktop\\sfz_zm.png");
        File out = new File("C:\\Users\\16479\\Desktop\\sfz_zm_test.png");
        File head = new File("C:\\Users\\16479\\Pictures\\test\\test.jpg");

        try (InputStream is = new FileInputStream(src);
             OutputStream os = new FileOutputStream(out);
             InputStream headIs = new FileInputStream(head)) {
            Image image = ImageIO.read(is);
            // 获取图片的高和宽
            int wideth = image.getWidth(null);
            int height = image.getHeight(null);
            // 新增一个图片缓冲
            BufferedImage bufferedImage = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, wideth, height, null);
            // 设置字体颜色（颜色也可以直接new定义rgba，例如new Color(17, 16, 44)）
            g.setColor(Color.BLACK);
            // size字体大小，Font.BOLD字体加粗
            g.setFont(new Font("宋体", Font.BOLD, 16));
            // 写入成果名称，由宽度减去我们测的宽度度，就等于要开始写的位置
            g.drawString("陈家亮", wideth - 410, height - 255); // 姓名
            g.drawString("男", wideth - 410, height - 220); // 性别
            g.drawString("汉", wideth - 300, height - 220); // 民族
            g.setFont(new Font("宋体", Font.BOLD, 18));
            g.drawString("1999", wideth - 410, height - 180); // 出生年
            g.drawString("10", wideth - 330, height - 180); // 出生月
            g.drawString("06", wideth - 280, height - 180); // 出生日
            g.drawString("江西省吉安市永新县龙门", wideth - 410, height - 140); // 住址
            g.drawString("镇黄岗村新炉下组14号", wideth - 410, height - 120); // 住址
            g.drawString("36243019991006XXXX", wideth - 340, height - 40); // 身份证号码
//            Image headImage = ImageIO.read(headIs);
//            g.drawImage(headImage, 330, 20, 80, 130, null);

            // 释放资源
            g.dispose();

            ImageIO.write(bufferedImage, "png", os);
        } catch (Exception e) {

        }
    }


}
