package com.jiniu.image.demo;

import org.apache.poi.util.IOUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.awt.Image.SCALE_AREA_AVERAGING;

/**
 * @author wuqh
 * @description  对InputStream流 进行 像素压缩
 * @date 2022/12/16 9:23
 */
public class imageUtilsFailedVersion {
    public static void main(String[] args) {
        System.out.println();
    }
    @Test
    public void testInputStreamUtils(){
        try {
            byte[] bytes = new byte[1024];
            File file = new File("93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            String name = file.getName();
            InputStream inputStream = new FileInputStream(file);
            //System.out.println("inputStream = " + inputStream);
            FileOutputStream outputStream = imageUtils(inputStream, file.getName());
            inputStream.close();
            // OutpuStream转换File测试是否 成功 压缩
            outputStream.write(bytes);
            outputStream.close();

            Files.write(Paths.get(System.getProperty("user.dir")+"scaling_"+file.getName()),bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testImageFileUtils(){
        try {
            byte[] bytes = new byte[1024];
            File file = new File("93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            String name = file.getName();
            FileInputStream inputStream = new FileInputStream(file);
            System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());
            //FileOutputStream outputStream = imageFileUtils(file);
            // OutpuStream转换File测试是否 成功 压缩
            //outputStream.write(bytes);
            //Files.write(Paths.get(System.getProperty("user.dir")+"scaling_"+file.getName()),bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 本地图片缩放
     * 3.改造传入的参数 为 InputStream 输出的OutputStream × 失败 因为改为流的形式读不到长宽
     * 4. 恢复之前的入参 File
     * */
    public FileOutputStream imageFileUtils(File file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file.getName());
            // 读取文件到bufferImag
            BufferedImage read = ImageIO.read(file);
            //BufferedImage read = ImageIO.read(stream);
            // 获取缩放宽高比例 默认当长宽都大于时
            int readWidth = read.getWidth();
            int readHeight = read.getHeight();
            Double scale = ((double) 1080 / readHeight > (double) 1440 / readWidth) ? (double) 1440 / read.getWidth() : (double) 1080 / read.getHeight();

            int width = (int) (readWidth * scale);
            int height = (int) (readHeight * scale);

            //缩放完成，还有一边超出范围
            while (width > 1440 || height > 1080) {
                width = (int) (width * scale);
                height = (int) (height * scale);
            }

            // 调用缩放后的图片 生成newImage
            Image newImage = read.getScaledInstance(width, height, SCALE_AREA_AVERAGING);
            // 创建新的缓存图片到bufferImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //获取画笔 copycode
            Graphics2D graphics = bufferedImage.createGraphics();
            //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
            graphics.drawImage(newImage, 0, 0, null);
            //一定要释放资源
            graphics.dispose();

            // 关键方法输出file / outPutStream
            ImageIO.write(bufferedImage, file.getName(), outputStream);
            //ImageIO.write(bufferedImage, fileName, outputStream);
            // inPutStream -> outPutStream
            // IOUtils.copy(inputStream,outputStream);
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 本地图片缩放
     * 3.改造传入的参数 为 InputStream 输出的OutputStream
     *
     * */
    public FileOutputStream imageUtils(InputStream stream,String fileName) {
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            // 读取文件到bufferImag
            //BufferedImage read = ImageIO.read(file);
            BufferedImage read = ImageIO.read(stream);
            outputStream.close();
            // 获取缩放宽高比例 默认当长宽都大于时
            int readWidth = read.getWidth();
            int readHeight = read.getHeight();
            Double scale = ((double) 1080 / readHeight > (double) 1440 / readWidth) ? (double) 1440 / read.getWidth() : (double) 1080 / read.getHeight();

            int width = (int) (readWidth * scale);
            int height = (int) (readHeight * scale);

            //缩放完成，还有一边超出范围
            while (width > 1440 || height > 1080) {
                width = (int) (width * scale);
                height = (int) (height * scale);
            }

            // 调用缩放后的图片 生成newImage
            Image newImage = read.getScaledInstance(width, height, SCALE_AREA_AVERAGING);
            // 创建新的缓存图片到bufferImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //获取画笔 copycode
            Graphics2D graphics = bufferedImage.createGraphics();
            //将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
            graphics.drawImage(newImage, 0, 0, null);
            //一定要释放资源
            graphics.dispose();

            // 关键方法输出file / outPutStream
            //ImageIO.write(bufferedImage, formateName, outPutFile);
            ImageIO.write(bufferedImage, fileName, outputStream);
            // inPutStream -> outPutStream
            // IOUtils.copy(inputStream,outputStream);
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
