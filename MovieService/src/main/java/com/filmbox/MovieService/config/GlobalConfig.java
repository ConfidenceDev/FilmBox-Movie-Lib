package com.filmbox.MovieService.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@OpenAPIDefinition
public class GlobalConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    // Allowing swagger UI to accept authorization token
    @Bean
    public OpenAPI customOpenAPI() {
        ApiResponse error = new ApiResponse()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().addExamples("default",
                                new Example().value("\"code\": 400, \"status:\" \"Bad request\", \"message:\" something went wrong!")
                        )))
                .description("Error");

        Contact contact = new Contact();
        contact.name("Confidence Dev");
        contact.url("https://github.com/ConfidenceDev");
        contact.email("confidostic3@gmail.com");

        Info info = new Info()
                .title("FilmBox Movie API")
                .description("API documentation for FilmBox Movie management system")
                .version("1.0.0")
                .contact(contact);

        Components components = new Components();
                /*.addSecuritySchemes("bearerAuth",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );*/
        components.addResponses("bad_request", error);
        return new OpenAPI()
                //.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(components).info(info);
    }
}
