package dev.captain.userservice.controller;


import dev.captain.userservice.model.tables.UniversityFeature;
import dev.captain.userservice.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-service/features")
public class FeatureController {

    private final FeatureService featureService;


    @GetMapping("")
    public ResponseEntity<?> getFeatures() {
        return ResponseEntity.ok(featureService.getFeatures());
    }


    @GetMapping("/university/{university-id}")
    public ResponseEntity<?> getFeaturesByUniversityId(@PathVariable("university-id") Long universityId) {
        return ResponseEntity.ok(featureService.getFeaturesByUniversityId(universityId));
    }


    @PostMapping("/university/{university-id}")
    public ResponseEntity<?> updateFeatures(@PathVariable("university-id") Long universityId,
                                            @RequestBody List<UniversityFeature> universityFeatures) {
        if (universityId == null)
            return ResponseEntity.badRequest().body("University id is required"
            );
        return ResponseEntity.ok(featureService.updateFeatures(universityFeatures, universityId));
    }
}
