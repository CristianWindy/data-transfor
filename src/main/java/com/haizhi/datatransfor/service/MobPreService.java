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
import java.util.HashMap;

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

    @Value("${url-suffix}")
    private String urlSuffix;

    @Autowired
    private EasyOKClient okClient;

    public String query(RequestBean bean, HttpServletRequest request, HttpServletResponse response) {
        log.info("MobPreService:捕获移动端转发请求：" + bean);
        String result = "";
        String path = bean.getPrePath() + urlSuffix;
        HashMap<String, Object> header = new HashMap<>();
        header.put("Authorization", bean.getAccessToken());
        result = okClient.jsonPost(path, bean, header, String.class, response, bean.getSource());
        return result;
    }

    public ResponseBean refreshToken(String accessToken) {
        log.info("MobPreService:捕获移动端刷新token请求：" + accessToken);
        String path = secondApiGateway + "/oauth2-server/oauth/refresh";
        HashMap<String, Object> params = new HashMap<>();
        params.put("refreshToken", accessToken);
        HashMap<String, Object> header = new HashMap<>();
        header.put("Authorization", accessToken);

        String s = okClient.jsonPost(path, params, header, String.class);
        JSONObject jsonObject = JSONObject.parseObject(s);
        ResponseBean responseBean = new ResponseBean();

        if (jsonObject.containsKey("code")) {
            if ("200".equals(jsonObject.getString("code"))) {
                responseBean.setStatus("0");
                responseBean.setErrstr("");
                responseBean.setTrcid("");
                jsonObject.getJSONObject("data");
                responseBean.setResult(jsonObject);
            } else {
                responseBean.setStatus(jsonObject.getString("code"));
                responseBean.setErrstr(jsonObject.getString("message"));
            }
        } else {
            responseBean.setStatus("1");
            responseBean.setErrstr("请求出错");
            responseBean.setResult(new JSONObject());
        }

        return responseBean;
    }

    public ResponseBean queryPath(String token, String packageName) {
        log.info("MobPreService:捕获移动端获取请求路径请求：token=" + token + "   packageName:" + packageName);
        String path = secondApiGateway + "/oauth2-server/app/address";
        HashMap<String, Object> params = new HashMap<>();
        params.put("packageName", packageName);
        HashMap<String, Object> header = new HashMap<>();
        header.put("Authorization", token);

        String s = okClient.jsonPost(path, params, header, String.class);
        JSONObject jsonObject = JSONObject.parseObject(s);
        ResponseBean responseBean = new ResponseBean();

        if (jsonObject.containsKey("code")) {
            if ("200".equals(jsonObject.getString("code"))) {
                responseBean.setStatus("0");
                responseBean.setErrstr("");
                responseBean.setTrcid("");
                jsonObject.getJSONArray("data");
                responseBean.setResult(jsonObject);
            } else {
                responseBean.setStatus(jsonObject.getString("code"));
                responseBean.setErrstr(jsonObject.getString("message"));
                responseBean.setResult(new JSONObject());
            }
        } else {
            responseBean.setStatus("1");
            responseBean.setErrstr("请求出错");
            responseBean.setResult(new JSONObject());
        }
        return responseBean;
    }

    public ResponseBean queryUserInfo(String token) {
        log.info("MobPreService:捕获移动端获取用户请求：" + token);
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
