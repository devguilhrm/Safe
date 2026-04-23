package com.limasoftware.Safety.config;

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
                        .title("Safety API - Sistema de Agendamento Médico")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de agendamentos médicos. " +
                                "Permite criar, editar, cancelar e concluir agendamentos, " +
                                "com validação de conflitos de horário e intervalo de datas.")
                        .termsOfService("https://github.com/seu-usuario/safety")
                        .contact(new Contact()
                                .name("Guilherme Lima")
                                .url("https://github.com/seu-usuario")
                                .email("seu-email@exemplo.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
