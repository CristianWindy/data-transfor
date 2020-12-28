package com.haizhi.datatransfor;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
            ImageIO.write(bufferedImage,"png",outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();

        String s= encoder.encode(outputStream.toByteArray());

        return s;

    }
}
