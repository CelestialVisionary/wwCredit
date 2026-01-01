package com.wwfinance.base.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

// @Configuration
public class OpenAPIConfig {

    // @Bean("baseApi")
    // @Primary
    public OpenAPI baseApi() {
        return new OpenAPI()
                .openapi("3.0.1")
                .info(new Info()
                        .title("finance-api API Docs")
                        .description("finance-api API Docs")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("威武金融")
                                .url("http://www.wwfinance.com")
                                .email("ww@wwfinance.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://wwfinance.com/LICENSE"))
                        .termsOfService("https://wwfinance.com/terms"));
    }
}