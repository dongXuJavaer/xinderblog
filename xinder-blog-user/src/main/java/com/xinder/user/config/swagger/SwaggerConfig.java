package com.xinder.user.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Xinder
 * @date 2023-02-17 10:49
 */
@Configuration
@EnableSwagger2// 开启Swagger2的自动配置
public class SwaggerConfig {

    @Bean //配置docket以配置Swagger具体参数
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // 2.设置扫描路径
                //.apis(RequestHandlerSelectors.basePackage("com.example.swaggerv2.controller"))
                .build();
    }
}
