package com.example.gatewayserver;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class SecurityFilter implements GatewayFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);
    private static final List<String> apiEndpoints = List.of("/auth/login", "/auth/validateToken");
    private static final Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                                                               .noneMatch(uri -> r.getURI().getPath().contains(uri));
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (isApiSecured.test(request)) {
            String jwtToken = getJwtFromRequest(request);
            if(jwtToken == null) {
                this.onError(exchange, "Authorization header is missing", HttpStatus.UNAUTHORIZED);
            }
            if(!isValid(jwtToken)) {
                this.onError(exchange, "Authorization header is not valid", HttpStatus.UNAUTHORIZED);
            }
        }
        return chain.filter(exchange);
    }

    private String getJwtFromRequest(ServerHttpRequest request) {
        String bearerToken = null;
        List<String> authorizationList = request.getHeaders().getOrEmpty("Authorization");
        if(!authorizationList.isEmpty()) {
            bearerToken = authorizationList.get(0);
        }
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public boolean isValid(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
