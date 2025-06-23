package br.pismo.techcase.flowbank.configuration.doc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Value("${app.http.resources.api-key-header}")
    private String xApiKey;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Flowbank API")
                .version("1.0.0")
                .description("API for Flowbank application operations")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
            .addSecurityItem(new SecurityRequirement().addList(xApiKey))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(xApiKey,
                    new SecurityScheme()
                        .name(xApiKey)
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .description("API Key required for authentication")));
    }
}
