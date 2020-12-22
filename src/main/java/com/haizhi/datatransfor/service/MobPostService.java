package com.haizhi.datatransfor.service;

import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.util.okhttp.EasyOKClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        log.info("MobPostService:获取到总线转发的请求："+bean);
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

        return result;
    }

    private String getRealityQueryPath(RequestBean bean) {
        String path="";
        String url="";
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
        }
        return url;
    }

    private String dealWithGetMethod(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        String url = getRealityQueryPath(bean);

        String result = okClient.get(url, bean.getRequestMap(), bean.getHeaderMap(), String.class, response, bean.getSource());

        return result;
    }
}
