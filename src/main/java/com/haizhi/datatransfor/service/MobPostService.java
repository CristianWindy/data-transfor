package com.haizhi.datatransfor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.haizhi.datatransfor.Base64ImgUtil;
import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.util.okhttp.EasyOKClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author windycristian
 * @Date: 2020/9/23 15:56
 **/
@Slf4j
@Service
public class MobPostService {
    @Value("${backend.url.bdp}")
    private String bdpUrl;
    @Value("${backend.url.pushCenter}")
    private String pushCenterUrl;

    @Value("${backend.url.tupu_login}")
    private String tupuLoginUrl;
    @Value("${backend.url.tupu_business}")
    private String tupuBusinessUrl;

    @Autowired
    private EasyOKClient okClient;

    public String query(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        log.info("MobPostService:获取到总线转发的请求：" + bean);
        String result = "";
        if ("get".equals(bean.getMethod())) {
            result = dealWithGetMethod(bean, request, response);
        } else if ("post".equals(bean.getMethod())) {
            result = dealWithPostMethod(bean, request, response);
        }
        return result;
    }

    private String dealWithPostMethod(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {

        String url = "";

        //BDP
        url = getRealityQueryPath(bean);

        String result = "";
        try {
            switch (bean.getContentType()) {
                //json
                case 0:
                    result = okClient.jsonPost(url, bean.getRequestMap(), bean.getHeaderMap(), String.class, response, bean.getSource());
                    break;
                //x-www-form
                case 1:
                    result = okClient.w3FormPost(url, bean.getRequestMap(), bean.getHeaderMap(), String.class, response, bean.getSource());
                    break;
                //form-data
                case 2:

                    break;
            }
        } catch (Exception e) {
            log.info("dealWithPostMethod:exception:{}", e.getMessage());
        }

        log.info("result:{}", result);

        return result;
    }

    private String getRealityQueryPath(RequestBean bean) {
        String path = "";
        String url = "";
        switch (bean.getSource()) {
            case 0:
                path = bdpUrl;
                url = path + "/" + bean.getPath();
                break;
            case 1:
                path = pushCenterUrl;
                url = path + bean.getPath();
                break;
            case 2:
                path = tupuLoginUrl;
                url = path + bean.getPath();
                break;
            case 3:
                path = tupuBusinessUrl;
                url = path + bean.getPath();
                break;
            case 4:
                url = bean.getPath();
        }
        return url;
    }

    private String dealWithGetMethod(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        String url = getRealityQueryPath(bean);
        String result = "";
        if (bean.getSource() == 4) {
            result = dealImage(url);
        } else {
            result = okClient.get(url, bean.getRequestMap(), bean.getHeaderMap(), String.class, response, bean.getSource());
        }

        return result;
    }

    public String dealImage(String imageUrl) {
        BASE64Decoder decoder = new BASE64Decoder();
        String path = null;
        try {
            path = new String(decoder.decodeBuffer(imageUrl), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = Base64ImgUtil.encodeImageToBase64(Base64ImgUtil.downloadPicture(path));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "OK");
        JSONObject data = new JSONObject();
        data.put("imageSource", s);
        jsonObject.put("data", data);

        return JSON.toJSONString(jsonObject);
    }

    public String scaleDealImage(String imageUrl) {
        BASE64Decoder decoder = new BASE64Decoder();
        String path = null;
        try {
            path = new String(decoder.decodeBuffer(imageUrl), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String s = Base64ImgUtil.compressEncodeImageToBase64(Base64ImgUtil.downloadPicture(path));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 0);
        jsonObject.put("msg", "OK");
        JSONObject data = new JSONObject();
        data.put("imageSource", s);
        jsonObject.put("data", data);

        return JSON.toJSONString(jsonObject);
    }
}
