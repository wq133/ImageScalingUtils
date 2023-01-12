package com.jiniu.image.demo;

import org.apache.poi.util.IOUtils;
import org.junit.Test;
import java.io.File;
import java.net.URL;

import com.jiniu.image.utils.ImageUtils;

/**
 * @author wuqh
 * @description 图片像素等比压缩 测试
 * @date 2022/12/16 14:27
 */
public class ImageScalingDemo {

    @Test
    public void scalingTest(){
        //String path = "C:/Users/wqh66/Desktop/rApk3WNPbmGATB-TAAdFIyh5gr0633.jpg";
        String path = "rApk3WNPbmGATB-TAAdFIyh5gr0633.jpg";
        File file = new File(path);
        File copyFile = ImageUtils.imageScaling(file);
        //System.out.println("file1.getPath() = " + copyFile.getAbsolutePath());
        //本地压缩图片删除
        ImageUtils.delCacheImage(copyFile);
    }

    @Test
    public void downLoadtest(){
        try {
            // <1440*1080
            URL url = new URL("https://we-saas-test.jiniutech.com/file/group1/M00/00/01/rApk3WNOTE6AJqf4AAAfA22Kaps069.png");

            // >1440*1080
            //URL url = new URL("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            //File file = ImageUtils.downloadAndImageScaling(url);
            //File file = ImageUtils.downloadAndDel(url);
            byte[] bytes = ImageUtils.downloadAndDel(url);
//            file.delete();

            System.out.println("bytes = " + bytes);
            //Boolean aBoolean = ImageUtils.delCacheImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTwo() {
//        String fileName = "IMG_20221208_210617.jpg";
//        ImageUtils.imageScalingtwo(new File(fileName));
//        String fileNameTwo = "e02c7ea64acce0cd749c4d5b50d9722e.jpeg";
//        ImageUtils.imageScalingtwo(new File(fileNameTwo));
//        String fileNameTwo = "企业微信截图_16732492972574.png";
//        ImageUtils.imageScalingtwo(new File(fileNameTwo));
        String fileNameTwo = "file_1672900830000手机银行交医保.jpg";
        ImageUtils.imageScalingtwo(new File(fileNameTwo));
//        ImageUtils.imageScaling(new File(fileNameTwo));

    }


}
