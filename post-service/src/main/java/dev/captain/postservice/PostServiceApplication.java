package dev.captain.postservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PostServiceApplication {



    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .filename(".env.dev")
                .load();

        System.setProperty("aws_access_key_id", dotenv.get("aws_access_key_id"));
        System.setProperty("aws_secret_access_key", dotenv.get("aws_secret_access_key"));
        System.setProperty("aws_region", dotenv.get("aws_region"));
        System.setProperty("MONGO_URI", dotenv.get("MONGO_URI"));



        SpringApplication.run(PostServiceApplication.class, args);
    }

}
