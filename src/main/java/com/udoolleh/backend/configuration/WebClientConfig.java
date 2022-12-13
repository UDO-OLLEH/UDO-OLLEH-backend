package com.udoolleh.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "udoOllehMicroserviceWebClient")
    public WebClient udoOllehMicroserviceWebClient() {
        return WebClient.create("http://localhost:8082");
    }
}
