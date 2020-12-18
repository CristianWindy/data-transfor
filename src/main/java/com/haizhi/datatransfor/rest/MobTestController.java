package com.haizhi.datatransfor.rest;

import com.alibaba.fastjson.JSONObject;
import com.haizhi.datatransfor.service.MobTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author windycristian
 * @Date: 2020/9/23 15:53
 **/
@Slf4j
@RestController
@RequestMapping("drs")
public class MobTestController {
    @Autowired
    private MobTestService baseService;

    @PostMapping("json-rpc")
    public JSONObject query(@RequestBody JSONObject body) {
        log.info("请求体：" + body);
        return baseService.query(body);
    }
}
