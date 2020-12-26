package com.haizhi.datatransfor.rest;

import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.service.MobPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author windycristian
 * @Date: 2020/9/23 15:53
 **/
@Slf4j
@RestController
@RequestMapping("post")
public class MobPostController {
    @Autowired
    private MobPostService baseService;

    @RequestMapping(value = "mob",produces = "application/json; charset=utf-8",method = RequestMethod.POST)
    public String query(@RequestBody RequestBean body,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        String query = baseService.query(body, request, response);
        log.info("query:{}", query);
        return query;
    }
}
