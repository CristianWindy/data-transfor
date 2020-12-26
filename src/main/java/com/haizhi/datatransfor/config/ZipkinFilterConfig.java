package com.haizhi.datatransfor.config;

import com.ga.base.zipkin.ZipkinFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

/**
 *
 * @author Enter
 *
 */
//@Configuration
public class ZipkinFilterConfig {

//	@Bean
	public FilterRegistrationBean chkFilterRegistration(){

		FilterRegistrationBean bean = new FilterRegistrationBean();
		bean.setFilter(new ZipkinFilter());
		bean.addUrlPatterns("/*");
		return bean;
	}
}
