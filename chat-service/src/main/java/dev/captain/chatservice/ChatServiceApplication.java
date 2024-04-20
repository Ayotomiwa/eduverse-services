package dev.captain.chatservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatServiceApplication {

    public static void main(String[] args) {


        Dotenv dotenv = Dotenv.configure()
                .filename(".env.dev")
                .load();
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));

        SpringApplication.run(ChatServiceApplication.class, args);
    }

}
