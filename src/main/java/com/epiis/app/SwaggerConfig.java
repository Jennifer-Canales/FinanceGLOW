package com.epiis.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API EPIIS")
                        .version("1.0")
                        .description("Documentación del proyecto de DS 2")
                        .contact(new Contact()
                                .name("Jennifer Canales Castillo")
                                .email("jennifercanalescastillo60@gmail.com"))
                       .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
               			.termsOfService("https://epiis.com/terminos"));
    }
}
