package org.mongodb.springboot.kitchensinkmordernization.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    public static final String JWT_WEB_TOKEN = "JwtWebToken";

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Kitchen Order Nization API"))
                .addSecurityItem(new SecurityRequirement().addList(JWT_WEB_TOKEN))
                .components(new Components().addSecuritySchemes(JWT_WEB_TOKEN, new SecurityScheme().name(JWT_WEB_TOKEN)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
                .info(new Info().title("My REST API")
                        .description("Some custom description of API.")
                        .version("1.0")
                        .contact(new Contact().name("Divya")
                                .email("divya.tiwari@mongodb.com")));
    }
}
