package com.haizhi.datatransfor.util.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.google.common.collect.Maps;
import com.haizhi.datatransfor.util.okhttp.ssl.TrustAllCerts;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @Author CoderJiA
 * @Description EasyOKClient
 * @Date 13/5/19 下午8:47
 **/
@Slf4j
@Component
@Scope("prototype")
public class EasyOKClient {

    public final OkHttpClient httpClient;

    /**
     * 连接超时时间 单位秒(默认10s)
     */
    private static final int CONNECT_TIMEOUT = 10;
    /**
     * 写超时时间 单位秒(默认 0 , 不超时)
     */
    private static final int WRITE_TIMEOUT = 0;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    private static final int READ_TIMEOUT = 120;
    /**
     * 底层HTTP库所有的并发执行的请求数量
     */
    private static final int DISPATCHER_MAX_REQUESTS = 32;
    /**
     * 底层HTTP库对每个独立的Host进行并发请求的数量
     */
    private static final int DISPATCHER_MAX_REQUESTS_PER_HOST = 8;
    /**
     * 底层HTTP库中复用连接对象的最大空闲数量
     */
    private static final int CONNECTION_POOL_MAX_IDLE_COUNT = 16;
    /**
     * 底层HTTP库中复用连接对象的回收周期（单位分钟）
     */
    private static final int CONNECTION_POOL_MAX_IDLE_MINUTES = 5;

    /**
     * 初始化默认连接池
     */
    private static final ConnectionPool DEFAULT_CONNETCTION_POOL = new ConnectionPool(CONNECTION_POOL_MAX_IDLE_COUNT, CONNECTION_POOL_MAX_IDLE_MINUTES, TimeUnit.MINUTES);


    public static final String CONTENT_TYPE_KEY = "content-type";
    public static final String CONTENT_TYPE_JSON = "application/json; charset=UTF-8";
    public static final String CONTENT_TYPE_W3FORM = "application/x-www-form-urlencoded";


    /**
     * 构建一个默认配置的 HTTP Client 类
     */
    public EasyOKClient() {
        this(
                CONNECT_TIMEOUT,
                READ_TIMEOUT,
                WRITE_TIMEOUT,
                DISPATCHER_MAX_REQUESTS,
                DISPATCHER_MAX_REQUESTS_PER_HOST,
                DEFAULT_CONNETCTION_POOL
        );
    }

    /**
     * 构建一个自定义配置的 HTTP Client 类
     */
    public EasyOKClient(int connTimeout, int readTimeout, int writeTimeout, int dispatcherMaxRequests,
                        int dispatcherMaxRequestsPerHost, ConnectionPool connectionPool) {

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(dispatcherMaxRequests);
        dispatcher.setMaxRequestsPerHost(dispatcherMaxRequestsPerHost);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.dispatcher(dispatcher);
        builder.connectionPool(connectionPool);
        builder.connectTimeout(connTimeout, TimeUnit.SECONDS);
        builder.readTimeout(readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);

        // 配置https
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.hostnameVerifier((s, sslSession) -> true);

        httpClient = builder
                .build();
    }

    /**
     * get
     */
    public <T> T get(String url, Map<String, Object> queryParams, Class<T> clazz) {
        return get(url, queryParams, Maps.newHashMap(), clazz);
    }

    /**
     * get
     */
    public <T> T get(String url, Map<String, Object> queryParams, Map<String, Object> headers, Class<T> clazz) {
        log.info("EasyOKClient请求地址：{}", url);
        log.info("EasyOKClient请求参数：{}", queryParams);
        log.info("EasyOKClient请求头：{}", headers);

        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        // 设置参数
        Optional.ofNullable(queryParams)
                .ifPresent(its -> its.forEach((key, value) -> httpBuilder.addQueryParameter(key, value.toString())));
        // 创建请求
        Request.Builder requestBuilder = new Request.Builder().get().url(httpBuilder.build());

        return JSON.parseObject(send(requestBuilder, headers), clazz);
    }

    /**
     * post
     * content-type: application/json
     */
    public <T> T jsonPost(String url, Map<String, Object> params, Class<T> clazz) {
        return jsonPost(url, params, Maps.newHashMap(), clazz);
    }

    public <T> T jsonPost(String url, String body, Class<T> clazz) {
        HashMap<String, Object> headers = Maps.newHashMap();
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        return post(url, getRequestBody(body.getBytes(StandardCharsets.UTF_8), headers), headers, clazz);
    }

    public <T> T jsonPost(String url, String body, Map<String, Object> headers, Class<T> clazz) {
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        return post(url, getRequestBody(body.getBytes(StandardCharsets.UTF_8), headers), headers, clazz);
    }

    public <T> T jsonPost(String url, Map<String, Object> params, Map<String, Object> headers, Class<T> clazz) {
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        log.info("EasyOKClient请求post参数：{}", params);
        return post(url, getRequestBody(JSON.toJSONString(params).getBytes(StandardCharsets.UTF_8), headers), headers, clazz);
    }

    public <T> T jsonPost(String url, Object object, Class<T> clazz, HttpServletResponse httpServletResponse, Integer source) {
        Map<String, Object> headers = new HashMap<>();
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_JSON);
        log.info("EasyOKClient请求post参数：{}", object);

        RequestBody body = getRequestBody(JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8), headers);

        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        Request.Builder requestBuilder = new Request.Builder().post(body).url(httpBuilder.build());

//        JSON.parseObject(send(requestBuilder, headers), clazz);

        return JSON.parseObject(send(requestBuilder, headers, httpServletResponse, source), clazz);

//        return post(url, getRequestBody(JSON.toJSONString(object).getBytes(StandardCharsets.UTF_8), headers), headers, clazz);
    }

    /**
     * post
     * content-type: application/x-www-form-urlencoded
     */
    public <T> T w3FormPost(String url, Map<String, Object> params, Class<T> clazz) {
        HashMap<String, Object> headers = Maps.newHashMap();
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_W3FORM);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = String.valueOf(params.get(key));
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request.Builder request = new Request
                .Builder()
                .post(formBody)
                .url(url);
        return JSON.parseObject(send(request, headers), clazz);
    }

    public <T> T w3FormPost(String url, Map<String, Object> params, Map<String, Object> headers, Class<T> clazz, HttpServletResponse httpServletResponse, Integer source) {
        headers.put(CONTENT_TYPE_KEY, CONTENT_TYPE_W3FORM);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            String value = String.valueOf(params.get(key));
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request.Builder request = new Request
                .Builder()
                .post(formBody)
                .url(url);
        return JSON.parseObject(send(request, headers, httpServletResponse, source), clazz);
    }


    private RequestBody getRequestBody(byte[] body, Map<String, Object> headers) {
        RequestBody rBody;
        if (body != null && body.length > 0) {
            MediaType mediaType = MediaType.parse(String.valueOf(headers.get(CONTENT_TYPE_KEY)));
            rBody = RequestBody.create(mediaType, body);
        } else {
            rBody = RequestBody.create(null, new byte[0]);
        }
        return rBody;
    }

    public <T> T post(String url, RequestBody body, Map<String, Object> headers, Class<T> clazz) {
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        Request.Builder requestBuilder = new Request.Builder().post(body).url(httpBuilder.build());
        return JSON.parseObject(send(requestBuilder, headers), clazz);
    }

    public JSONObject formPost(String url, HashMap<String, String> params, String header) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<Map.Entry<String, String>> set = params.entrySet();
        for (Map.Entry<String, String> entry : set) {
            if (!StringUtils.isEmpty(entry.getKey()) && !StringUtils.isEmpty(entry.getValue())) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.post(formBodyBuilder.build());
        builder.removeHeader("Cookie");

        Map<String, Object> headers = new HashMap<>();
        if (header != null && !StringUtils.isEmpty(header)) {
            headers.put("Cookie", header);
        }
        return sendForm(builder, headers);
    }


    public JSONObject sendForm(Request.Builder requestBuilder, Map<String, Object> headers) {
        Response response;
        try {
            Optional.ofNullable(headers)
                    .ifPresent(its -> its.forEach((key, value) -> requestBuilder.header(key, value.toString())));
            response = httpClient.newCall(requestBuilder.build()).execute();
        } catch (Exception ex) {
            throw new RuntimeException("调用远程接口【" + requestBuilder.build().url().toString() + "】失败", ex);
        }
        try {
            assert response.body() != null;
            String responseStr = response.body().string();
            Headers headers1 = response.headers();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < headers1.size(); i++) {
                if (headers1.name(i).toLowerCase().equals("set-cookie")) {
                    String cookie = headers1.value(i);
                    String val = cookie.substring(0, cookie.indexOf(";"));
                    stringBuffer.append(val).append("; ");
                }
            }
            if (stringBuffer.length() > 0) {
                stringBuffer.append("domain=").append("haizhi").append(";");
            }


            JSONObject object = new JSONObject();
            object.put("result", responseStr);
            object.put("header", stringBuffer.toString());

            return object;
        } catch (Exception e) {
            throw new RuntimeException("请求接口【" + requestBuilder.build().url() + "】失败", e);
        }
    }

    private String send(Request.Builder requestBuilder, Map<String, Object> headers) {
        log.info("调用远程接口【" + requestBuilder.build().url().toString() + "】");
        log.info("请求方法" + requestBuilder.build().method());
        log.info("请求体" + requestBuilder.build().body().toString());
        Response response;
        try {
            Optional.ofNullable(headers)
                    .ifPresent(its -> its.forEach((key, value) -> requestBuilder.header(key, value.toString())));
            response = httpClient.newCall(requestBuilder.build()).execute();
        } catch (Exception ex) {
            throw new RuntimeException("调用远程接口【" + requestBuilder.build().url().toString() + "】失败", ex);
        }
        try {
            assert response.body() != null;
            String responseStr = response.body().string();
            log.info("EasyOKClient请求返回值：{}", responseStr);
            return responseStr;
        } catch (Exception e) {
            throw new RuntimeException("请求接口【" + requestBuilder.build().url() + "】失败", e);
        }
    }

    private String send(Request.Builder requestBuilder, Map<String, Object> headers, HttpServletResponse httpServletResponse, Integer source) {
        log.info("调用远程接口【" + requestBuilder.build().url().toString() + "】");
        log.info("请求方法" + requestBuilder.build().method());
        log.info("请求体" + requestBuilder.build().body().toString());
        Response response;
        try {
            Optional.ofNullable(headers)
                    .ifPresent(its -> its.forEach((key, value) -> requestBuilder.header(key, value.toString())));
            response = httpClient.newCall(requestBuilder.build()).execute();
        } catch (Exception ex) {
            throw new RuntimeException("调用远程接口【" + requestBuilder.build().url().toString() + "】失败", ex);
        }
        Headers responseHeaders = response.headers();

        if (source == 0) {
            List<String> values = responseHeaders.values("Set-Cookie");
            values.forEach(data -> {
                httpServletResponse.addHeader("Set-Cookie", data);
            });
        } else {
            for (int i = 0; i < responseHeaders.size(); i++) {
                String headerName = responseHeaders.name(i);
                String headerValue = responseHeaders.get(headerName);
                httpServletResponse.addHeader(headerName, headerValue);
            }
        }

        try {
            assert response.body() != null;
            String responseStr = response.body().string();
            log.info("EasyOKClient请求返回值：{}", responseStr);
            return responseStr;
        } catch (Exception e) {
            throw new RuntimeException("请求接口【" + requestBuilder.build().url() + "】失败", e);
        }
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }


}
