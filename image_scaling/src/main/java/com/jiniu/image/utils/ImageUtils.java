package com.jiniu.image.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.awt.Image.SCALE_AREA_AVERAGING;

/**
 * @author wuqh
 * @description 图片工具类
 * @date 2022/12/16 14:22
 */
@Slf4j
public class ImageUtils {
    /**
     * 判断删除 哪里的图片 是否成功
     */
    public static Boolean delCacheImage(File file){
        // 删除1个/两个文件
        // 从传入文件的路径判断
        boolean isdelete = false;
        String name = file.getName();
        System.out.println(name);
        if (file.getAbsolutePath().contains(System.getProperty("user.dir"))){
            isdelete = file.delete();
            System.out.println("删除Url压缩图片成功"+file.getName());
        }else {
            isdelete = file.delete();
            System.out.println("删除本地的压缩图片成功"+file.getName());
        }
        return isdelete;
        // 是否contain 根路径 是->删除两个 否 删除缓存的
    }

    /**
     * 本地图片缩放
     * 1.等比缩放
     * 2.本地图片orUrl图片
     * */
    public static File imageScaling(File file) {
        try {
            // 读取文件到bufferImag
            BufferedImage read = ImageIO.read(file);
            // 获取缩放宽高比例 默认当长宽都大于时
            int readWidth = read.getWidth();
            int readHeight = read.getHeight();
            // 获取对应宽高比
            Double scale = ((double)1080/readHeight > (double)1440/readWidth) ?
                    (double)1440/read.getWidth() : (double)1080/read.getHeight();
            int width = (int) (readWidth * scale);
            int height = (int) (readHeight * scale);
            // 缩放完成，若还有一边超出范围 继续缩放
            while (width > 1440 || height > 1080){
                width = (int) (width * scale);
                height = (int) (height * scale);
            }

            // --------------------cv核心代码段start--------------
            // 调用缩放后的图片 newImage
            Image newImage = read.getScaledInstance(width, height, SCALE_AREA_AVERAGING);
            // 创建新的缓存图片 bufferImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 获取画笔 copycode
            Graphics2D graphics = bufferedImage.createGraphics();
            // 将Image对象画在画布上,最后一个参数,ImageObserver:接收有关 Image 信息通知的异步更新接口,没用到直接传空
            graphics.drawImage(newImage, 0, 0,null);
            // 一定要释放资源
            graphics.dispose();
            // --------------------cv核心代码段end--------------

            // 获取文件名and类型后缀
            String name = file.getName();
            String formateName = name.substring((name.indexOf(".") + 1));
            // 获取pathName 直接传name的不会包含path
            String pathName = (!file.getPath().contains("\\")) ?
                    System.getProperty("user.dir") : Arrays.stream(file.getPath().split(file.getName())).collect(Collectors.toList()).get(0);
            //System.out.println("pathName:"+pathName);

            // 根据pathName判断是本地上传还是url上传
            File copyFile = (pathName.contains(System.getProperty("user.dir"))) ?
                    new File("scaling_" +name) : new File(pathName+"scaling_" +name);

            // 核心代码 bufferedImage写入copyFile
            ImageIO.write(bufferedImage,formateName, copyFile);
            return copyFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Url图片
     * 1.将Url ->InputStream ->本地File
     * 2.调用本地File缩放
     * @param url
     * @return
     */
    public static File downloadAndImageScaling(URL url) {
        try {
            String[] strings = url.getPath().split("/");
            String imageName = strings[strings.length-1];
            //本地File init
            File downloadFile = new File(System.getProperty("user.dir")+"/"+imageName);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpConn.getInputStream();

            // 实现stream流转换成File对象的形式实现图片的缩放 （从而满足上传 条件）
            copyInputStreamToFile(url.openStream(), downloadFile);
            // 调用 本地图片缩放方法
            File copyFile = imageScaling(downloadFile);
            downloadFile.delete();
            // 压缩后的inputStream
            //inputStream = new FileInputStream(copyFile);
            // 转换成bytes字节数组
            //byte[] bytes = IoUtils.toByteArray(inputStream);

            return copyFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     InputStream -> File
     */
    private static File copyInputStreamToFile(InputStream inputStream, File file) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            // commons-io
            IOUtils.copy(inputStream, outputStream);
            return file;
        }
    }

}
