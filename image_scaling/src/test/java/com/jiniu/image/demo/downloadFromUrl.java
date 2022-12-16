package com.jiniu.image.demo;


import com.alibaba.excel.util.IoUtils;
import org.apache.poi.util.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author wuqh
 * @description downloadFromUrl 从链接下载到本地
 * @date 2022/12/14 17:47
 */
public class downloadFromUrl {
    public static void main(String[] args) {
        //byte[] bytes = downloadByUrl("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");

    }

    @Test
    public void copyTest(){
        // 需要把inputStream（url中的）转为file到本地
        // initFile
        String outPutPath = "E:/jiniu_Work/backend/utils/";
        File file = new File(outPutPath);

        try {
            // 完成url转换成inputStream
            URL url = new URL("https://we-saas-cloud-cos.jiniutech.com/saas/dev/2022/11/30/93c7fc7a-cd26-488f-9d9f-f3c57ccaae41.jpg");
            String file1 = url.getFile();
            String[] strings = url.getPath().split("/");
            String imageName = strings[strings.length-1];
            file = new File(outPutPath+imageName);

            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpConn.getInputStream();

            // 1.完成把inputStream转换成file对象
            file = copyInputStreamToFile(inputStream, file);
            // 2.图片缩放
            File file2  = scalingDemo.scalingImage(file, outPutPath);

            System.out.println("file2.getPath() = " + file2.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载到本地
     * @param sourceUrl
     * @param localPath
     * @return
     */
    public static byte[] downloadByUrl(String sourceUrl,String localPath) {
        try {
            URL url = new URL(sourceUrl);
            File file = new File(localPath);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpConn.getInputStream();
            // 实现stream流转换成File对象的形式实现图片的缩放 （从而满足上传 条件）
            copyInputStreamToFile(url.openStream(),file);
            // 调用图片缩放工具类
            return IoUtils.toByteArray(inputStream);
        } catch (Exception e) {

        }
        return null;
    }



    // InputStream -> File
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

