package com.jiniu.image.demo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import static java.awt.Image.*;

/**
 * @author wuqh
 * @description 图片缩放utils
 * @date 2022/12/14 14:58
 */
public class scalingDemo {
    public static void main(String[] args) {
        File file = pathScaling();

    }

    /**
     * 本地文件上传缩放
     * @return
     */
    public static File pathScaling( ){
        String sourcePath = "C:/Users/wqh66/Pictures/Links/2022-06-02_183301.png";
        File file = new File(sourcePath);
        System.out.println(file.getAbsolutePath());
        String outPutPath = "E:/jiniu_Work/backend/utils/";

        File outPutFile = scalingImage(file, outPutPath);

        if (outPutFile != null) return outPutFile;
        return null;
    }
    // 图片缩放Utils
    public static File scalingImage(File file, String outPutPath) {
        try {
            // 读取文件到bufferImag
            BufferedImage read = ImageIO.read(file);
            // 获取缩放宽高
           /*
            Double scale = 0.8;
            int width = (int) (read.getWidth() * scale);
            int height = (int) (read.getHeight() * scale);
            while (width > 1440 || height >1080){
                width = (int) (read.getWidth() * scale);
                height = (int) (read.getHeight() * scale);
            }
            */
            // 调用缩放后的图片 生成newImage
            Image newImage = read.getScaledInstance(1440, 1080, SCALE_AREA_AVERAGING);
            // 创建新的缓存图片到bufferImage
            BufferedImage bufferedImage = new BufferedImage(1440, 1080, BufferedImage.TYPE_INT_RGB);

            //获取画笔 copycode
            Graphics2D graphics = bufferedImage.createGraphics();
            //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
            graphics.drawImage(newImage, 0, 0,null);
            //一定要释放资源
            graphics.dispose();
            // 获取文件名and类型后缀
            String name = file.getName();
            String formateName = name.substring((name.indexOf(".") + 1));

            File outPutFile = new File(outPutPath +"scaling_" +name);
            ImageIO.write(bufferedImage,formateName, outPutFile);
            return outPutFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
