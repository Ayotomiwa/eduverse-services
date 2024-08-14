package dev.captain.apigateway.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class LocalAppConfig {
    @PostConstruct
    public void setup() {
        Dotenv dotenv = Dotenv.configure().filename(".env.dev").load();
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
    }
}