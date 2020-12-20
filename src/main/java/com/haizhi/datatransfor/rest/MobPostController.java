package com.haizhi.datatransfor.rest;

import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.service.MobPostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    @PostMapping("mob")
    public String query(@RequestBody RequestBean body,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        return baseService.query(body,request,response);
    }
}
