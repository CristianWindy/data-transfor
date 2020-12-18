package com.haizhi.datatransfor.rest;

import com.alibaba.fastjson.JSONObject;
import com.haizhi.datatransfor.service.DataTransformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author windycristian
 * @Date: 2020/9/21 17:21
 **/
@Slf4j
@RestController
@RequestMapping("data_transform")
public class DataTransformController {
    @Autowired
    private DataTransformService baseService;

    @PostMapping("test_rpc")
    public JSONObject requestToBDP(@RequestBody JSONObject object, HttpServletResponse response) {
        log.info("请求体：" + object.toJSONString());
        JSONObject result = baseService.requestToBDP(object);
        return result;
    }
}
