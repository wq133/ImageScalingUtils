package com.jiniu.image.demo;

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
            URL url = new URL("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            File file = ImageUtils.downloadAndImageScaling(url);
            //Boolean aBoolean = ImageUtils.delCacheImage(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
