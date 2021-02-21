package com.example.demo.openapi;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.service.Contact;
import java.util.Collections;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Sareeta Ecommerce API",
                "Ecommerce API , Udaicty Project",
                "0.1-alpa",
                "http://www.udacity.com/tos",
                new Contact("Suresh Thomas", "www.udacity.com", "suresh.thomas@email-private.com"),
                "License of API", "http://www.udacity.com/license", Collections.emptyList());
    }

}
