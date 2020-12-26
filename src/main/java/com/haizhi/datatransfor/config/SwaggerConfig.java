package com.haizhi.datatransfor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
    public Docket createRestApi() {

		ParameterBuilder param = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<Parameter>();
		param.name("requestType").description("请求类型")
		.modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		pars.add(param.build());
		Docket docket =  new Docket(DocumentationType.SWAGGER_2);
		docket.useDefaultResponseMessages(false);
		docket.genericModelSubstitutes(DeferredResult.class);
		docket.forCodeGeneration(true);
		docket.pathMapping("/");
		docket.groupName("集成服务总线SDK服务demo")
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.haizhi.datatransfor.rest"))
                .paths(PathSelectors.any())
                .build().globalOperationParameters(pars);

        return docket;
    }

	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfoBuilder().title("集成服务总线SDK服务demo测试")
				.description("集成服务总线SDK服务demo")
				.contact(new Contact("zuozhe", "", "xxx@qq.com")).version("1.0").build();
		return apiInfo;
	}
}
