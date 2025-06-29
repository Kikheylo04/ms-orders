package com.example.ms_orders.client;

import com.example.ms_orders.dto.ProductDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ProductClientTest {

    private ProductClient productClient;
    private MockRestServiceServer mockServer;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        productClient = new ProductClient(restTemplate);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void testGetProductById_ReturnsProductDto() {
        Long productId = 1L;
        String url = "http://localhost:8080/api/products/" + productId;
        String responseBody = "{\"id\":1,\"name\":\"Café\",\"price\":10}";

        mockServer.expect(requestTo(url))
                .andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        ProductDto result = productClient.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Café", result.getName());
        assertEquals(new java.math.BigDecimal("10"), result.getPrice());
        mockServer.verify();
    }

    @Test
    void testGetProductById_NotFound() {
        Long productId = 2L;
        String url = "http://localhost:8080/api/products/" + productId;

        mockServer.expect(requestTo(url))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        ProductDto result = productClient.getProductById(productId);

        assertNull(result);
        mockServer.verify();
    }
}
