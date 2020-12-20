package com.haizhi.datatransfor.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author windycristian
 * @Date: 2020/9/21 18:26
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBean {
    /**
     * result : {}
     * trcid : 68f40367-f1d6-317b-a9d3-eb16e5d2ae89
     * errstr :
     * status : 0
     */
    private Object result;
    private String trcid;
    private String errstr;
    private String status;
}
