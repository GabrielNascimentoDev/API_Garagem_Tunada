package com.senac.garagem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "API Garagem Tunada", version = "1.0",
        description = "API RESTful de carros modificados: fabricantes, donos, fichas de dinamômetro e modificações"))
public class GaragemApplication {
    public static void main(String[] args) {
        SpringApplication.run(GaragemApplication.class, args);
    }
}
