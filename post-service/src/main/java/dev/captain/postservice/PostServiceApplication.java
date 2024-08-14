package dev.captain.postservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class PostServiceApplication {



    public static void main(String[] args) {

        SpringApplication.run(PostServiceApplication.class, args);
    }

}
