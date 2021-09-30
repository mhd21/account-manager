package com.mpakbaz.accountManager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

        @Bean
        public Docket docket() {
                return new Docket(DocumentationType.SWAGGER_2)

                                .apiInfo(new ApiInfoBuilder().title("Account Manager API")
                                                .description("A CRUD API for Account Manager").version("0.0.1-SNAPSHOT")
                                                .license("MIT").licenseUrl("https://opensource.org/licenses/MIT")
                                                .build())
                                .enable(true)
                                .tags(new Tag("Account", "Endpoints for CRUD operations on accounts"),
                                                new Tag("Customer", "Endpoints for CRUD operations on customers"),
                                                new Tag("Transaction", "Endpoints for CRUD operations on transactions"))
                                .select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                                .build();
        }

}
