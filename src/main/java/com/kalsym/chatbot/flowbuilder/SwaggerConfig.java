package com.kalsym.chatbot.flowbuilder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 *
 * @author 7cu
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                // .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.kalsym.chatbot.flowbuilder"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("product-service")
                .description(
                        "exposes the product endpoints")
                .termsOfServiceUrl("TBA")
                .license("TBA")
                .licenseUrl("")
                .version(VersionHolder.VERSION)
                .build();
    }
}
