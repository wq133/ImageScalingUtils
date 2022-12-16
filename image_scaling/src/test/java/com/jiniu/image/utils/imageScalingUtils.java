package com.jiniu.image.utils;

import org.apache.poi.util.IOUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Collectors;
import static java.awt.Image.SCALE_AREA_AVERAGING;

/**
 * @author wuqh
 * @description 整合图片缩放： 1.传入Url 2. 传入File
 * @date 2022/12/15 9:23
 */
public class imageScalingUtils {
    /**
     * Url上传测试
     */
    @Test
    public void urlScalingImageTest(){
        try {
            //String localpath = "C:/Users/Public/Pictures";
            URL url = new URL("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            downloadAndImageScaling(url);
            //System.out.println("bytes = " + bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 本地上传测试
     * 2.test只传文件名 可行性？ 可行 根路径 user.dir
     */
    @Test
    public void scalingTest(){
//        String sourcePath = "C:/Users/wqh66/Pictures/Links/2022-06-02_183301.png";
//        File file = new File(sourcePath);
//        File file1 = imageScaling(file);
//        System.out.println("file1.getPath() = " + file1.getPath());
        String path = "C:/Users/wqh66/Desktop/rApk3WNPbmGATB-TAAdFIyh5gr0633.jpg";
        File file = new File(path);
        File copyFile = imageScaling(file);
        System.out.println("file1.getPath() = " + copyFile.getAbsolutePath());
        // 获取pathName

    }

    /**
        等比缩放
        width和height 与 1440和1080 取比 取最小的比。
        若两边第一次缩放后仍有一边大于微信限制 再次缩放。
     */
    @Test
    public void caculateNumber(){
        File file = new File("C:/Users/wqh66/Desktop/rApk3WNPbmGATB-TAAdFIyh5gr0633.jpg");
        File file1 = imageScaling(file);
        System.out.println(file1.getPath());
    }


    /**
     * 切分出路径test
     * @return
     */
    @Test
    public void pathTest( ){
        String sourcePath = "C:/Users/wqh66/Pictures/Links/2022-06-02_183301.png";
        File file = new File(sourcePath);
        // 获取绝对路径
        System.out.println(file.getAbsolutePath());
        // 获取文件路径
        String split = Arrays.stream(file.getPath().split(file.getName())).collect(Collectors.toList()).get(0);
        System.out.println(split);
        //切出sourcePath 除去文件名
        //File outPutFile = scalingImage(file, outPutPath);
    }

    /**
     * 删除测试after压缩后
     */
    @Test
    public void deleteTest(){
        // localImageTest
//        String localPath = "C:/Users/wqh66/Desktop/";
//        String path = "93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg";
//        File file = new File(localPath+path);
//        System.out.println(file.getAbsolutePath());
//        File copyFile = imageScaling(file);
//        Boolean aBoolean = delCacheImage(copyFile);
        // UrlImageTest

        try {
            String localpath = "C:/Users/Public/Pictures";
            URL url = new URL("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            File copyFile = downloadAndImageScaling(url);

            // 删除文件test
            Boolean aBoolean = delCacheImage(copyFile);
            if (aBoolean){
                System.out.println("压缩完成后删除成功");
            }else {
                System.out.println("删除失败！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除本地压缩的图片
     */
    public Boolean delCacheImage(File file){
        // 删除1个/两个文件
        // 从传入文件的路径判断
        boolean isdelete = false;
        String name = file.getName();
        System.out.println(name);
        if (file.getAbsolutePath().contains(System.getProperty("user.dir"))){
            System.out.println("删除Url压缩图片成功");
            isdelete = file.delete();
        }else {
            System.out.println("删除本地的压缩图片成功");
            isdelete = file.delete();
        }
        return isdelete;
        // 是否contain 根路径 是->删除两个 否 删除缓存的

    }

    /**
     * 本地图片缩放
     * 1.等比缩放 改造完成
     *
     * */
    public File imageScaling(File file) {

        //String outPutPath = Arrays.stream(file.getPath().split(file.getName())).collect(Collectors.toList()).get(0);
        try {

            // 读取文件到bufferImag
            BufferedImage read = ImageIO.read(file);
            // 获取缩放宽高比例 默认当长宽都大于时
            int readWidth = read.getWidth();
            int readHeight = read.getHeight();
            Double scale = ((double)1080/readHeight > (double)1440/readWidth) ?
                    (double)1440/read.getWidth() : (double)1080/read.getHeight();

            int width = (int) (readWidth * scale);
            int height = (int) (readHeight * scale);

            //缩放完成，还有一边超出范围
            while (width > 1440 || height > 1080){
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
            graphics.drawImage(newImage, 0, 0,null);
            //一定要释放资源
            graphics.dispose();
            // 获取文件名and类型后缀
            String name = file.getName();
            String formateName = name.substring((name.indexOf(".") + 1));
            //File outPutFile = new File(outPutPath +"scaling_" +name);

            // 获取pathName 直接传name的不会包含path
            String pathName = (!file.getPath().contains("\\")) ?
                    System.getProperty("user.dir") : Arrays.stream(file.getPath().split(file.getName())).collect(Collectors.toList()).get(0);
            System.out.println("pathName:"+pathName);
            // 根据pathName判断是本地上传还是url上传
            File copyFile = (pathName.contains(System.getProperty("user.dir"))) ?
                    new File("scaling_" +name) : new File(pathName+"scaling_" +name);

            //System.out.println("outPutFile.getPath() = " + outPutFile.getPath());
            ImageIO.write(bufferedImage,formateName, copyFile);

            //IOUtils.copy(inputStream,);
            return copyFile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Url图片压缩
     * 1.将Url ->InputStream ->本地File
     * 2.调用本地File压缩
     * @param url
     * @return
     */
    public File downloadAndImageScaling(URL url) {
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
