package dev.captain.apigateway.config.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> PUBLIC_URLS = List.of(
            "/api/user-service/university/register",
            "/api/user-service/authenticate",
            "/api/users/reset-password",
            "/api/chat-service/ws/info",
            "/api/chat-service/ws/",
            "/api/chat-service/"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> PUBLIC_URLS.stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));


}
