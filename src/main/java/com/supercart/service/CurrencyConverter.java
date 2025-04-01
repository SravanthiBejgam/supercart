package com.supercart.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyConverter {

    private final RestTemplate restTemplate;
    private static final String EXCHANGE_RATE_URL = "https://api.frankfurter.app/latest?from=USD&to={currency}";

    public CurrencyConverter(@Qualifier("frankfurterRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BigDecimal convertUsdToCurrency(BigDecimal usdAmount, String currency) {
        if (currency.equalsIgnoreCase("USD")) {
            return usdAmount;
        }
        Map<String, Object> response = restTemplate.getForObject(EXCHANGE_RATE_URL, Map.class, currency);
        Map<String, Double> rates = (Map<String, Double>) response.get("rates");
        double rate = rates.get(currency.toUpperCase());
        return usdAmount.multiply(BigDecimal.valueOf(rate));
    }
}