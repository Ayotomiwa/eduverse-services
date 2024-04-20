package dev.captain.postservice.s3;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;

@Configuration
public class S3Config {

    @Bean
    public Region region() {
        return Region.EU_WEST_2;
    }

}