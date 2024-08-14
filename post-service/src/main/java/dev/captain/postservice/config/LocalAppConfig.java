package dev.captain.postservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("default")
public class LocalAppConfig {
    @PostConstruct
    public void setup() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env.dev")
                .load();

        System.setProperty("aws_access_key_id", dotenv.get("aws_access_key_id"));
        System.setProperty("aws_secret_access_key", dotenv.get("aws_secret_access_key"));
        System.setProperty("aws_region", dotenv.get("aws_region"));
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));

    }
}