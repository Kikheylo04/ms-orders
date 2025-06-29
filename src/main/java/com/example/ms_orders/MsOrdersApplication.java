package com.example.ms_orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MsOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrdersApplication.class, args);
	}
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
