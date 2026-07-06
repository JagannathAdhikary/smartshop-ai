package com.smartshop.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ProductClientConfig {

    @Bean
    RestClient restClient(LoadBalancerClient loadBalancerClient) {
        return RestClient.builder()
                .baseUrl("http://product-service")
                .requestInterceptor(
                        new LoadBalancerInterceptor(loadBalancerClient))
                .build();
    }
}
