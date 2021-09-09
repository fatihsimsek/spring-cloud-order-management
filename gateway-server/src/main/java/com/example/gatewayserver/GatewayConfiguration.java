package com.example.gatewayserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

    @Autowired
    private SecurityFilter filter;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/auth/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH-SERVICE"))
                .route(r -> r.path("/order/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ORDER-SERVICE"))
                .route(r -> r.path("/product/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://PRODUCT-SERVICE"))
                .build();
    }
}
