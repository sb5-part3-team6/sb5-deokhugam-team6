package com.codeit.project.deokhugam.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public WebClient naverWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://openapi.naver.com")
                .build();
    }
}
