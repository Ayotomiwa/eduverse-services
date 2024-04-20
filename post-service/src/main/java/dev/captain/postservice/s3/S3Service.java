package dev.captain.postservice.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
public class S3Service {
    private final String accessKeyId;
    private final String secretKey;
    private final String region;

    public S3Service(@Value("${aws_access_key_id}") String accessKeyId,
                     @Value("${aws_secret_access_key}")String secretKey,
                     @Value("${aws_region}") String region) {

        this.accessKeyId = accessKeyId;
        this.secretKey = secretKey;
        this.region = region;
    }

    public String createPresignedPutUrl(String bucketName, String keyName) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(this.accessKeyId, this.secretKey);
        try (S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of(this.region))
                .build()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(30))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            System.out.println("presignedRequest.url().toExternalForm() = " + presignedRequest.url().toExternalForm());
            return presignedRequest.url().toExternalForm();
        }
    }
}