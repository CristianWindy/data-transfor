package com.haizhi.datatransfor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.WebAppRootListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebAppRootContext implements ServletContextInitializer {

	@Value("${spring.application.name}")
	private String servicesName;

	@Value("${serviceID}")
	private String serviceID;

	@Value("${server.port}")
	private String serverPort;

	@Value("${secretKey}")
	private String secretKey;

//	@Value("${networkMode}")
//	private String networkMode;

//	@Value("${operationMode}")
//	private String operationMode;

	@Value("${server.url}")
	private String serverUrl;

	//手动配置到注册中心的ip和端口和开关true/false
	@Value("${localswitch:}")
	private String localswitch;
	@Value("${localip:}")
	private String localip;
//	@Value("${localport:}")
//	private String localport;
//	//是否走网关注册中心服务注册true,false
//	@Value("${IS_SERVER_URL_REGISTER:}")
//	private String IS_SERVER_URL_REGISTER;

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addListener(WebAppRootListener.class);
		/*
		 * 服务名称
		 */
        servletContext.setInitParameter("servicesName", servicesName);//这里是注入参数
        /*
		 * 服务ID
		 */
        servletContext.setInitParameter("serviceID", serviceID);//这里是注入参数

        /*
		 * 服务端口(注：默认是8080)
		 */
        servletContext.setInitParameter("serverPort", serverPort);
        /*
		 * 服务密钥
		 */
        servletContext.setInitParameter("secretKey", secretKey);
		/*
		 * 网络环境的切换:JMT 移动专网,SZGAW 公安网,默认是公安网SZGAW
		 */
//        servletContext.setInitParameter("networkMode", networkMode);
		/*
		 * 运行模式:dev 开发模式,prod 生产模式,test 测试模式,默认是测试模式 test
		 */
//        servletContext.setInitParameter("operationMode", operationMode);

        //配置网关地址
        servletContext.setInitParameter("serverUrl", serverUrl);

        //手动配置到注册中心的ip和端口和开关true/false
        servletContext.setInitParameter("localswitch", localswitch);
        servletContext.setInitParameter("localip", localip);
//        servletContext.setInitParameter("localport", localport);
        //是否走网关注册中心服务注册true,false
//        servletContext.setInitParameter("IS_SERVER_URL_REGISTER", IS_SERVER_URL_REGISTER);

	}

}
