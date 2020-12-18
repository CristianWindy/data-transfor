package com.haizhi.datatransfor.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haizhi.datatransfor.util.okhttp.EasyOKClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import java.util.HashMap;
import java.util.Set;

/**
 * @Author windycristian
 * @Date: 2020/9/21 17:40
 **/
@Slf4j
@Service
public class DataTransformService {
    @Value("${bdp.url}")
    private String bdpUrl;

    @Value("${json.rpc.sourceId}")
    private String sourceId;

    @Autowired
    private EasyOKClient okClient;

    public JSONObject requestToBDP(JSONObject object) {
        if ("connect".equals(object.getString("method"))) {
            return returnConnectQuery(object);
        } else if ("query".equals(object.getString("method"))) {
            if (object.containsKey("params")) {
                JSONObject params = object.getJSONObject("params");
                if (params.containsKey("data")) {
                    JSONObject data = params.getJSONObject("data");
                    if (data.containsKey("condition")) {
                        String condition = data.getString("condition");

                        String[] split = condition.split("'");
                        if (split.length > 0) {
                            String url = split[1];
                            String method = split[3];
                            String header = split[5];
                            String mapStr = split[7].trim();
                            BASE64Decoder decoder = new BASE64Decoder();
                            try {
                                mapStr = new String(decoder.decodeBuffer(mapStr), "UTF-8");
                            } catch (Exception e) {
                                log.error("base64解码异常：" + mapStr);
                            }
                            JSONObject map = JSONObject.parseObject(mapStr);
                            JSONObject result;
                            if ("post".equals(method.toLowerCase().trim())) {
                                result = postToBDP(url, map, header);
                            } else {
                                result = getToBDP(url, map, header);
                            }
                            return decorateResult(url, object, result);
                        } else {
                            log.info("split的length<=0");
                        }
                    } else {
                        log.info("没有condition结构");
                    }
                } else {
                    log.info("没有data结构");
                }
            } else {
                log.info("没有params结构");
            }
        }
        return null;
    }

    private JSONObject returnConnectQuery(JSONObject object) {
        JSONObject result = new JSONObject();
        result.put("id", object.getString("id"));
        result.put("jsonrpc", object.getString("jsonrpc"));

        JSONObject resultBean = new JSONObject();
        resultBean.put("code", 1);
        resultBean.put("msg", "OK");
        resultBean.put("sign", "");

        JSONObject data = new JSONObject();
//        data.put("appId", object.getJSONObject("params").getJSONObject("data").getString("appId"));
        data.put("appId", "088a6391076c479abd0a5d4c20f5db72");
        data.put("timestamp", System.currentTimeMillis());
//        data.put("nonce", object.getJSONObject("params").getJSONObject("data").getString("nonce"));
        data.put("nonce", "222d5a857fb947ca9ff945732312a17d");
        data.put("sessionId", "6aed0804698d4a5b9fd23fff138826ee");
        resultBean.put("data", data);
        result.put("result", resultBean);
        result.put("id", object.getString("id"));
        return result;
    }

    private JSONObject decorateResult(String url, JSONObject object, JSONObject result) {
        JSONObject resultObject = new JSONObject();

        try {
            resultObject.put("id", object.getString("id"));
            resultObject.put("jsonrpc", object.getString("jsonrpc"));

            JSONObject resultbean = new JSONObject();
            resultbean.put("code", 1);
            resultbean.put("msg", "");
            resultbean.put("sign", "");

            JSONArray data = new JSONArray();
            JSONObject dataBean = new JSONObject();
            dataBean.put("sourceId", sourceId);
            JSONArray fieldValues = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("field", "url");
            jsonObject.put("value", url);
            jsonObject.put("codeValue", "");
            jsonObject.put("isCode", 0);
            fieldValues.add(jsonObject);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("field", "method");
            jsonObject2.put("value", "post");
            jsonObject2.put("codeValue", "");
            jsonObject2.put("isCode", 0);
            fieldValues.add(jsonObject2);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("field", "header");
            jsonObject3.put("value", "header");
            jsonObject3.put("codeValue", "");
            jsonObject3.put("isCode", 0);
            fieldValues.add(jsonObject3);

            JSONObject jsonObject4 = new JSONObject();
            jsonObject4.put("field", "map");
            jsonObject4.put("value", result.toJSONString());
            jsonObject4.put("codeValue", "");
            jsonObject4.put("isCode", 0);
            fieldValues.add(jsonObject4);


            dataBean.put("fieldValues", fieldValues);

            data.add(dataBean);


            resultbean.put("data", data);
            resultObject.put("result", resultbean);
            log.info("result:" + resultObject.toJSONString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return resultObject;
    }

    private JSONObject getToBDP(String url, JSONObject map, String header) {

        return null;
    }

    private JSONObject postToBDP(String url, JSONObject map, String header) {
        HashMap<String, String> map1 = new HashMap<>();
        Set<String> strings = map.keySet();
        strings.forEach(key -> {
            map1.put(key, map.getString(key));
        });

        JSONObject s = okClient.formPost(bdpUrl + url, map1, header);
        return s;
    }

    public static void main(String[] args) {
        String condition = "url='/mob/user/login' and method='post' and header='' and map='{\\\"domain\\\": \\\"haizhi\\\",\\\"username\\\": \\\"liuleilei\\\",\\\"password\\\": \\\"haizhi1234\\\"}'";
        String[] split = condition.split("'");

        System.out.println("url:" + split[1]);
        System.out.println("method:" + split[3]);
        System.out.println("header:" + split[5]);
        System.out.println("map:" + split[7]);
    }
}
