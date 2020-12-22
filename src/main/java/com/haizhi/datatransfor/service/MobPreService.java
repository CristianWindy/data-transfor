package com.haizhi.datatransfor.service;

import com.alibaba.fastjson.JSONObject;
import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.bean.ResponseBean;
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
public class MobPreService {
    @Value("${backend.url.bdp}")
    private String bdpUrl;
    @Value("${backend.url.pushCenter}")
    private String pushCenterUrl;

    @Value("${api.gateway_second}")
    private String secondApiGateway;

    @Autowired
    private EasyOKClient okClient;

    public String query(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        log.info("MobPreService:捕获移动端转发请求："+bean);
        String result = "";
        String path = bean.getPrePath();
        result = okClient.jsonPost(path, bean, String.class,response,bean.getSource());
        return result;
    }

    public ResponseBean refreshToken(String accessToken) {
        log.info("MobPreService:捕获移动端刷新token请求："+accessToken);
        String path = secondApiGateway + "/oauth2-server/oauth/refresh";

        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("0");
        responseBean.setErrstr("");
        responseBean.setTrcid("");


        JSONObject jsonObject = new JSONObject();
        jsonObject.put("access_token", "guhijokjhgfghjk");
        jsonObject.put("refresh_token", "gjapsdjhajskdasdhj");
        jsonObject.put("expires", 12312);

        responseBean.setResult(jsonObject);
        return responseBean;
    }

    public ResponseBean queryPath(String token, String packageName) {
        log.info("MobPreService:捕获移动端获取请求路径请求：token="+token+"   packageName:"+packageName);
        String path = secondApiGateway + "/oauth2-server/app/address";


        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("0");
        responseBean.setErrstr("");
        responseBean.setTrcid("");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "http://192.168.43.6:8110/api/post/mob");
        jsonObject.put("serviceId", "ashdasd");
        responseBean.setResult(jsonObject);

        return responseBean;
    }

    public ResponseBean queryUserInfo(String token) {
        log.info("MobPreService:捕获移动端获取用户请求："+token);
        String path = secondApiGateway + "/oauth2-server/oauth/user";

        ResponseBean responseBean = new ResponseBean();
        responseBean.setStatus("0");
        responseBean.setErrstr("");
        responseBean.setTrcid("");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idCard", "342423199009104496");
        jsonObject.put("packageName", "com.haizhi.ml");
        jsonObject.put("deptcode", "3600000001");
        jsonObject.put("userid", "911120");
        jsonObject.put("deptName", "测试机构");
        jsonObject.put("username", "buzhidao");
        responseBean.setResult(jsonObject);
        return responseBean;
    }
}
