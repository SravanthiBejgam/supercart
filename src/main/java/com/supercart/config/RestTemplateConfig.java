package com.supercart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import java.util.Base64;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "productServiceRestTemplate")
    public RestTemplate productServiceRestTemplate(
            @Value("${product.service.username}") String username,
            @Value("${product.service.password}") String password
    ) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(getBasicAuthInterceptor(username, password));
        return restTemplate;
    }

    private ClientHttpRequestInterceptor getBasicAuthInterceptor(String username, String password) {
        return (request, body, execution) -> {
            String auth = username + ":" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            request.getHeaders().add("Authorization", "Basic " + encodedAuth);
            return execution.execute(request, body);
        };
    }

    @Bean
    public RestTemplate frankfurterRestTemplate() {
        return new RestTemplate();
    }
}