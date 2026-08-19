package br.com.sentinela.core_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestClient;

@EnableAsync
@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClient(){
        return RestClient.builder().baseUrl("http://localhost:8000").build();
    }
}
