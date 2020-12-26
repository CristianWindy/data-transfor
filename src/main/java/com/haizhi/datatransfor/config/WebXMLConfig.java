package com.haizhi.datatransfor.config;


import com.ga.registry.EurekaClientInitListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebXMLConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebXMLConfig.class);

	@Bean
	public ServletListenerRegistrationBean<EurekaClientInitListener> servletListenerRegistrationBean() {
		ServletListenerRegistrationBean<EurekaClientInitListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<EurekaClientInitListener>();
		/*
		 * 注：
			1、服务只能注册到一个注册中心。
			2、注册到Eureka注册中心的服务是不能调用注册到Zookeeper注册中心的服务。
			3、注册到Zookeeper注册中心的服务可以调用注册到Eureka注册中心的服务，通过http请求，
		 */
		servletListenerRegistrationBean.setListener(new EurekaClientInitListener());
		return servletListenerRegistrationBean;
	}

}
