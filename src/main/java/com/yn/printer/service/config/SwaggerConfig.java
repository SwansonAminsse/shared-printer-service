package com.yn.printer.service.config;

import com.yn.printer.service.common.consts.HeaderConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 **/
@Configuration
public class SwaggerConfig {

    @Value("local")
    private String active;

    @Bean
    public Docket customDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        List<RequestParameter> requestParameterList = new ArrayList<>();
        RequestParameterBuilder userTokenRequestParameterBuilder = new RequestParameterBuilder();
        userTokenRequestParameterBuilder
                .name(HeaderConst.MEMBER_TOKEN_HEADER_NAME)
                .description("小程序会员令牌")
                .in(ParameterType.HEADER)
                .required(false)
                .build();
        requestParameterList.add(userTokenRequestParameterBuilder.build());

        docket = docket.apiInfo(apiInfo())
                .enable(true)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yn.printer"))
                .paths(PathSelectors.any())
                .build()
                .globalRequestParameters(requestParameterList);
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("yn-shared-printer-service API中心")
                .description("API接口文档")
                .contact(new Contact("yn", "http://yn-ce.com", "hhb@yn-ce.com"))
                .version("1.0")
                .build();
    }
}
