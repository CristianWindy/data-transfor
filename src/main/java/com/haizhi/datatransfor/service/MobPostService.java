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

    @Autowired
    private EasyOKClient okClient;

    public String query(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        if ("get".equals(bean.getMethod())) {
            result = dealWithGetMethod(bean, request, response);
        } else if ("post".equals(bean.getMethod())) {
            result = dealWithPostMethod(bean, request, response);
        }
        return result;
    }

    private String dealWithPostMethod(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        String path = "";
        //BDP
        switch (bean.getSource()) {
            case 0:
                path = bdpUrl;
                break;
            case 1:
                path = pushCenterUrl;
                break;
            default:
                path = bdpUrl;
                break;
        }
        String url = path + "/" + bean.getPath();

        String result = "";
        switch (bean.getContentType()) {
            //json
            case 0:
                result = okClient.jsonPost(url, bean.getRequestMap(), bean.getHeaderMap(), String.class);
                break;
            //x-www-form
            case 1:
                result = okClient.w3FormPost(url, bean.getRequestMap(), bean.getHeaderMap(), String.class, response, bean.getSource());
                break;
            //form-data
            case 2:

                break;
            default:

                break;
        }

        return result;
    }

    private String dealWithGetMethod(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {

        return null;
    }
}
