package dev.captain.userservice.service;


import dev.captain.userservice.model.tables.Feature;
import dev.captain.userservice.model.tables.University;
import dev.captain.userservice.model.tables.UniversityFeature;
import dev.captain.userservice.repo.FeatureRepo;
import dev.captain.userservice.repo.UniversityFeatureRepo;
import dev.captain.userservice.repo.UniversityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeatureService {

    private final FeatureRepo featureRepo;
    private final UniversityFeatureRepo universityFeatureRepo;
    private final UniversityRepo universityRepo;


    public List<Feature> getFeatures() {
        return featureRepo.findAll();
    }

    public List<UniversityFeature> getFeaturesByUniversityId(Long universityId) {
        return universityFeatureRepo.findByUniversityId(universityId);
    }


    public List<UniversityFeature> updateFeatures(List<UniversityFeature> universityFeatures, Long universityId) {
        University university = universityRepo.findById(universityId).orElse(null);
        if (university == null) {
            return null;
        }
        for (UniversityFeature universityFeature : universityFeatures) {
            universityFeature.setUniversity(university);
        }
        return universityFeatureRepo.saveAll(universityFeatures);
    }
}

