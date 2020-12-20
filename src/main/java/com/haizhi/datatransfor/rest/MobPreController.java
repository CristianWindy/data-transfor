package com.haizhi.datatransfor.rest;

import com.haizhi.datatransfor.bean.RequestBean;
import com.haizhi.datatransfor.bean.ResponseBean;
import com.haizhi.datatransfor.service.MobPostService;
import com.haizhi.datatransfor.service.MobPreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author windycristian
 * @Date: 2020/9/23 15:53
 **/
@Slf4j
@RestController
@RequestMapping("pre")
public class MobPreController {
    @Autowired
    private MobPreService baseService;

    /**
     * 转发移动端接口
     *
     * @param body
     * @param request
     * @param response
     * @return
     */
    @PostMapping("mob")
    public String query(@RequestBody RequestBean body,
                        HttpServletRequest request,
                        HttpServletResponse response) {

        return baseService.query(body, request, response);
    }

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @GetMapping("refresh_token")
    public ResponseBean refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return baseService.refreshToken(refreshToken);
    }

    /**
     * 资源服务寻址
     *
     * @return
     */
    @GetMapping("query_path")
    public ResponseBean queryPath(@RequestParam String token,
                                  @RequestParam String packageName) {
        return baseService.queryPath(token, packageName);
    }

    /**
     * 获取用户信息
     *
     * @param accessToken
     * @return
     */
    @GetMapping("query_user")
    public ResponseBean queryUserInfo(@RequestParam String accessToken) {
        return baseService.queryUserInfo(accessToken);
    }
}
