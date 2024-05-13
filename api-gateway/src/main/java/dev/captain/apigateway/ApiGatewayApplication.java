package dev.captain.apigateway;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .filename(".env.dev")
                .load();

        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

}
