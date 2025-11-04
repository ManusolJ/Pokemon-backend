package com.pokemon.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
