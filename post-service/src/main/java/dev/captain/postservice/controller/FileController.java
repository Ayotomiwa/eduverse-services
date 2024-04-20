package dev.captain.postservice.controller;

import dev.captain.postservice.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post-service")
public class FileController {

    private final S3Service s3Service;

    @GetMapping("/presigned-url")
    public ResponseEntity<String> generatePresignedUrl(@RequestParam String bucketName, @RequestParam String keyName) {
        System.out.println("keyName" + keyName);
        System.out.println("bucketName: " + bucketName);
        String presignedUrl = s3Service.createPresignedPutUrl(bucketName, keyName);
        return ResponseEntity.ok(presignedUrl);
    }
}