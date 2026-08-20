package br.com.sentinela.core_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${ai.gateway.url}")
    private String aiGatewayUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(aiGatewayUrl)
                .build();
    }
}