package dev.captain.userservice.controller;


import dev.captain.userservice.model.dto.UniversityDTO;
import dev.captain.userservice.model.tables.University;
import dev.captain.userservice.repo.AppAdminRepo;
import dev.captain.userservice.service.UniversityService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-service/university")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;
    private final AppAdminRepo appAdminRepo;


    @PostMapping("/register")
    ResponseEntity<?> addUniversity(@RequestBody UniversityDTO universityDTO) {

        ResponseEntity<String> response = getUniversityCheckResponse(universityDTO);
        if (response != null) {
            return response;
        }
        return ResponseEntity.ok(universityService.save(universityDTO));
    }


    @PostMapping("/update")
    ResponseEntity<?> updateUniversity(@RequestBody University university) {
        if (university == null) {
            return ResponseEntity.badRequest().body("University cannot be null");
        }
        if (university.getName() == null) {
            return ResponseEntity.badRequest().body("University name cannot be null");
        }
        return ResponseEntity.ok(universityService.updateUniversity(university));
    }


    @GetMapping("")
    ResponseEntity<?> getUniversities(
            @PathParam("company-id") String companyID) {
        if (companyID == null || !appAdminRepo.existsById(companyID)) {
            return ResponseEntity.badRequest().body("You are not Authorized to perform this operation");
        }
        return ResponseEntity.ok(universityService.getUniversities());
    }

    @GetMapping("/{university-id}")
    ResponseEntity<?> getUniversity(@PathVariable("university-id") Long universityId) {
        if (!universityService.existsUniversity(universityId)) {
            return ResponseEntity.badRequest().body("University does not exist");
        }
        return ResponseEntity.ok(universityService.getUniversity(universityId));
    }


    @PostMapping("/{university-id}/verify")
    ResponseEntity<?> verifyUniversity(@PathVariable("university-id") Long universityId,
                                       @RequestParam("company-id") String companyID
    ) {
        if (!universityService.existsUniversity(universityId)) {
            return ResponseEntity.badRequest().body("University does not exist");
        }

        if (companyID == null || !appAdminRepo.existsById(companyID)) {
            return ResponseEntity.badRequest().body("You are not Authorized to perform this operation");
        }
        universityService.verifyUniversity(universityId);
        return ResponseEntity.ok("University verified");
    }

    @PostMapping("/{university-id}/theme")
    ResponseEntity<?> saveTheme(@PathVariable("university-id") Long universityId,
                                @RequestParam("primary") String primaryTheme,
                                @RequestParam("secondary") String secondaryTheme
    ) {
        System.out.println("primary: " + primaryTheme + " secondary: " + secondaryTheme);
        if (!universityService.existsUniversity(universityId)) {
            return ResponseEntity.badRequest().body("University does not exist");
        }
        universityService.saveTheme(universityId, primaryTheme, secondaryTheme);
        return ResponseEntity.ok("Theme changed to " + primaryTheme + " & " + secondaryTheme);
    }


    @Nullable
    private ResponseEntity<String> getUniversityCheckResponse(UniversityDTO university) {
        ResponseEntity<String> nullCheckResponse = getNullCheckResponse(university);
        if (nullCheckResponse != null) {
            return nullCheckResponse;
        }
        if (universityService.existsUniversity(university.getUniversityName())) {
            return ResponseEntity.badRequest().body("University already exists");
        }
        return null;
    }


    @Nullable
    private ResponseEntity<String> getNullCheckResponse(UniversityDTO university) {
        if (university == null) {
            return ResponseEntity.badRequest().body("University cannot be null");
        }
        if (university.getUniversityName() == null) {
            return ResponseEntity.badRequest().body("University name cannot be null");
        }
        return null;
    }

}
