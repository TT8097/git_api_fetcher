package com.example.demo;

import com.example.demo.services.UrlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;

@Configuration
public class Configurations {
    @Bean
    public RestTemplate httpClient() {
        return new RestTemplate();  // Register HttpClient as a Spring Bean
    }
    @Bean
    public UrlService urlService() {
        return new UrlService();  // Register HttpClient as a Spring Bean
    }
}
