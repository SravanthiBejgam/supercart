package com.supercart.service;

import com.supercart.model.Product;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

@Service
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private static final String PRODUCT_SERVICE_URL = "https://product-service.herokuapp.com/api/v1/products";

    /*public ProductServiceClient(@Qualifier("productServiceRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }*/


    public ProductServiceClient(
            @Qualifier("productServiceRestTemplate") RestTemplate restTemplate
    ) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getAllProducts() {
        return restTemplate.exchange(
                PRODUCT_SERVICE_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Product>>() {}
        ).getBody();
    }
}