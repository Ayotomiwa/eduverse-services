package dev.captain.userservice.service;

import dev.captain.userservice.model.dto.AppUserDTO;
import dev.captain.userservice.model.dto.StaffDTO;
import dev.captain.userservice.model.dto.UniversityDTO;
import dev.captain.userservice.model.enums.AUTHORITY;
import dev.captain.userservice.model.tables.Feature;
import dev.captain.userservice.model.tables.University;
import dev.captain.userservice.model.tables.UniversityFeature;
import dev.captain.userservice.repo.FeatureRepo;
import dev.captain.userservice.repo.StudentRepo;
import dev.captain.userservice.repo.UniversityRepo;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepo universityRepo;
    private final FeatureRepo featureRepo;
    private final StudentRepo studentRepo;
    private final UserService userService;

    public boolean existsUniversity(String name) {
        return universityRepo.existsUniversityByName(name.toLowerCase());
    }

    public void setDefaultSettings(University university) {
        List<Feature> features = featureRepo.findAll();

        for (Feature feature : features) {
            UniversityFeature universityFeature = new UniversityFeature();
            universityFeature.setFeature(feature);
            universityFeature.setAuthorizedUsers(AUTHORITY.STANDARD);
            universityFeature.setAuthorizedUsers(AUTHORITY.ADMIN);
            universityFeature.setEnabled(true);
            universityFeature.setUniversity(university);
            universityFeature.setFeature(feature);
//            if (university.getUniversityFeatures() == null) {
//                university.setUniversityFeatures(new ArrayList<>());
//            }
//            university.getUniversityFeatures().add(universityFeature);
//            if (feature.getUniversityFeatures() == null) {
//                feature.setUniversityFeatures(new ArrayList<>());
//            }
            feature.getUniversityFeatures().add(universityFeature);
        }

    }


    public University save(UniversityDTO universityDTO) {
        University university = new University();
        university.setName(universityDTO.getUniversityName());
        university.setDomain(universityDTO.getDomain());
        university.setPhoneNumber(universityDTO.getPhoneNumber());
        university.setPrimaryTheme("#000000");
        university.setSecondaryTheme("#000000");
        university.setVerified(true);
        university = universityRepo.save(university);
        setDefaultSettings(university);
        AppUserDTO appUserDTO = createAppUserDTO(universityDTO);
        userService.create(appUserDTO, university.getId());
        return university;
    }


    public AppUserDTO createAppUserDTO(UniversityDTO universityDTO) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setFirstName(universityDTO.getFirstName());
        appUserDTO.setLastName(universityDTO.getLastName());
        appUserDTO.setEmail(universityDTO.getEmail());
        appUserDTO.setPassword(universityDTO.getUserPassword());
        appUserDTO.setAuthority(AUTHORITY.ADMIN);
        appUserDTO.setUserType("STAFF");
        StaffDTO staffDTO = new StaffDTO();
        staffDTO.setStaffType("NON_TEACHING");
        appUserDTO.setStaff(staffDTO);
        return appUserDTO;
    }

    public void verifyUniversity(Long universityId) {
        University university = universityRepo.findUniversityById(universityId);
        university.setVerified(true);
    }


    public University updateUniversity(University university) {
        University existingUniversity = universityRepo.findUniversityById(university.getId());

        if (university.getLogoUrl() != null && !university.getLogoUrl().startsWith("http")) {
            String logoUrl = "https://eduverse-v1.s3.eu-west-2.amazonaws.com/" + university.getId() + "/" + university.getLogoUrl();
            university.setLogoUrl(logoUrl);
        }

        existingUniversity.updateUniversityFields(university);
        return universityRepo.save(existingUniversity);
    }

    public boolean existsUniversity(Long universityId) {
        return universityRepo.existsUniversityById(universityId);
    }

    public University retrieveUniversity(Long universityId) {
        return universityRepo.findUniversityById(universityId);
    }

    public List<University> getUniversities() {
        return universityRepo.findAll();
    }

    public University getUniversity(Long universityId) {
        return universityRepo.findUniversityById(universityId);
    }

    public void saveTheme(Long universityId, String primaryTheme, String secondaryTheme) {
        University university = universityRepo.findUniversityById(universityId);
        university.setPrimaryTheme(primaryTheme);
        university.setSecondaryTheme(secondaryTheme);
        universityRepo.save(university);
    }

    public boolean existsStudent(Long studentId) {
        return studentRepo.existsById(studentId);
    }

    public boolean existsStaff(Long staffId) {
        return studentRepo.existsById(staffId);
    }
}
