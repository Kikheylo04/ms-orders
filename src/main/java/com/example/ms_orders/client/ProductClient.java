package com.example.ms_orders.client;

import com.example.ms_orders.dto.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductClient {

    private final RestTemplate restTemplate;

    public ProductClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDto getProductById(Long productId) {
        String url = "http://localhost:8080/api/products/" + productId;
        try {
            return restTemplate.getForObject(url, ProductDto.class);
        } catch (org.springframework.web.client.HttpClientErrorException.NotFound e) {
            return null;
        }
    }
}
