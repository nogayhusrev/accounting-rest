package com.nogayhusrev.accounting_rest;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class AccountingRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountingRestApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                        .title("Accounting Application")
                        .version("v3")
                        .description("Accounting Application API documentation"))
                .servers(List.of(new Server().url("localhost:8081").description("Dev Environment")));
    }



}
