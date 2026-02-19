package edu.yacoubi.tasks.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("🗂️ Task Management API")
                        .version("v1.0-demo")
                        .description("API zur Verwaltung von Aufgabenlisten und Tasks. Demo für Arbeitgeber & Portfolio.")
                        .contact(new Contact()
                                .name("A. El Yacoubi")
                                .email("demo@gmail.com")
                                .url("https://github.com/AhmedElyacoubiForJ"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
