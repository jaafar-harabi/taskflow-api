package com.example.taskflow.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Taskflow API")
                        .version("1.0.0")
                        .description("Project & Task management backend"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("taskflow")
                .pathsToMatch("/api/**")
                .build();
    }
}
