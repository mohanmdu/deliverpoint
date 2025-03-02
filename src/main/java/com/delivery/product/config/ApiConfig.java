package com.delivery.product.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev", "test"})
public class ApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().components(new Components())
                .info(new Info().title("Delivery API Implementation").description("This is a doc for API"));
    }
}
