package com.haizhi.datatransfor.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author windycristian
 * @Date: 2020/9/21 16:11
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestBean {
    //请求方法：get，post
    private String method;
    //请求地址
    private String path;

    //0-bdp,1-pushCenter
    private Integer source;

    //请求方式
    private Integer contentType;

    //请求头
    private Map<String, Object> headerMap;
    //参数
    private Map<String, Object> requestMap;

    private String prePath;
}
