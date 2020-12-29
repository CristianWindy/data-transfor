package com.haizhi.datatransfor;

import com.ga.base.utils.StringUtils;
import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author windycristian
 * @Date: 2020/12/26 17:40
 **/
public class Base64ImgUtil {

    public static String encodeImageToBase64(String remark) {
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(remark);
            BufferedImage bufferedImage = ImageIO.read(url);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String s = encoder.encode(outputStream.toByteArray());
        return s;
    }


    public static String compressEncodeImageToBase64(String remark) {
        String s = compressPicForScale(remark, filePath, 400, 0.8);

        return getImageStr(s);
    }

    /**
     * 对本地图片进行base64编码
     *
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }


    public static final String filePath = System.getProperty("user.dir") + "/src/test.png";

    //链接url下载图片
    public static String downloadPicture(String urlList) {
        URL url = null;
        int imageNumber = 0;
        try {
            url = new URL(urlList);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            String imageName = filePath;

            FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            byte[] context = output.toByteArray();
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    /**
     * 压缩
     *
     * @param srcPath
     * @param desPath
     * @param desFileSize
     * @param accuracy
     * @return
     */
    public static String compressPicForScale(String srcPath, String desPath,
                                             long desFileSize, double accuracy) {
        if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(srcPath)) {
            return null;
        }
        if (!new File(srcPath).exists()) {
            return null;
        }
        try {
            File srcFile = new File(srcPath);
            long srcFileSize = srcFile.length();
            System.out.println("源图片：" + srcPath + "，大小：" + srcFileSize / 1024
                    + "kb");
            // 1、先转换成jpg
            Thumbnails.of(srcPath).scale(1f).toFile(desPath);
            //按照比例进行缩放
            imgScale(desPath, desFileSize, accuracy);

            File desFile = new File(desPath);
            System.out.println("目标图片：" + desPath + "，大小" + desFile.length()
                    / 1024 + "kb");
            System.out.println("图片压缩完成！");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return desPath;
    }

    /**
     * 按照比例进行缩放
     */
    private static void imgScale(String desPath, long desFileSize,
                                 double accuracy) throws IOException {
        File file = new File(desPath);
        long fileSize = file.length();
        //判断大小，如果小于指定大小，不压缩；如果大于等于指定大小，压缩
        if (fileSize <= desFileSize * 1024) {
            return;
        }
        //按照比例进行缩小
        Thumbnails.of(desPath).scale(accuracy).toFile(desPath);//按比例缩小
        System.out.println("按照比例进行缩放");
        imgScale(desPath, desFileSize, accuracy);
    }


}
