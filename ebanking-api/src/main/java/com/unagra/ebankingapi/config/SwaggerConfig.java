package com.unagra.ebankingapi.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("UNAGRA - eBanking API")
                                .summary("eBanking API")
                                .description("This collection is dedicated to show each service that are referred accounts saved for the customer.")
                                .version("v0.0.1")
                                .termsOfService("This collection is only for exclusive use of  UNAGRA.")
                                .contact(
                                        new Contact()
                                                .name("Iván H. Botello Fermoso")
                                                .email("ibotello@unagra.com.mx")
                                )
                                .license(
                                        new License()
                                                .name("Apache 2.0")
                                                .url("http://springdoc.org")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("SpringShop Wiki Documentation")
                                .url("https://springshop.wiki.github.org/docs")
                );
    }
}
