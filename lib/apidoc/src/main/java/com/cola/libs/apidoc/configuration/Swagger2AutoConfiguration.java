package com.cola.libs.apidoc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by jiachen.shi on 6/22/2017.
 */
@Configuration
@EnableSwagger2
public class Swagger2AutoConfiguration {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.cola"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Cola RESTful APIs")
                .description("Cola Platform Description：https://github.com/james-shijiachen/cola")
                .termsOfServiceUrl("https://github.com/james-shijiachen/cola")
                .contact(new Contact("james.shi","","jiachen.shi@accenture.com"))
                .version("1.0")
                .build();
    }

}
