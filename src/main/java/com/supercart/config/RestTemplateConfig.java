package com.supercart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate productServiceRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate frankfurterRestTemplate() {
        return new RestTemplate();
    }
}