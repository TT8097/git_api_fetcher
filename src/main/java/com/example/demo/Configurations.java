package com.example.demo;

import com.example.demo.services.UrlService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class Configurations {
    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();  // Register HttpClient as a Spring Bean
    }
    @Bean
    public UrlService urlService() {
        return new UrlService();  // Register HttpClient as a Spring Bean
    }
}
