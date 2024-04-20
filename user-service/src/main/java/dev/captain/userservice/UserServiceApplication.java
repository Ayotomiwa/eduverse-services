package dev.captain.userservice;

import dev.captain.userservice.model.enums.*;
import dev.captain.userservice.model.tables.*;
import dev.captain.userservice.repo.*;
import dev.captain.userservice.service.CustomUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("Features")
    public CommandLineRunner create(FeatureRepo featureRepo,
                                    UniversityRepo universityRepo,
                                    ProfileInfoRepo profileInfoRepo,
                                    UserRepo userRepo,
                                    AppAdminRepo adminRepo,
                                    DepartmentRepo departmentRepo,
                                    CourseRepo courseRepo,
                                    StudentRepo studentRepo,
                                    StaffRepo staffRepo,
                                    PasswordEncoder bCryptPasswordEncoder,
                                    UniversityFeatureRepo universityFeatureRepo

    ) {

        return (args) -> {
            createFeatures(featureRepo, universityFeatureRepo);
            createUsers(featureRepo, universityRepo, profileInfoRepo,
                    userRepo,
                    departmentRepo,
                    courseRepo,
                    studentRepo,
                    staffRepo,
                    bCryptPasswordEncoder,
                    universityFeatureRepo


            );
            createAdminUser(adminRepo);

        };
    }


    @Bean
    public AuthenticationManager authenticationManager(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }


    public void createAdminUser(AppAdminRepo adminRepo) {

        GodAdmin godAdmin = new GodAdmin();
        godAdmin.setCompanyID("3420062");
        godAdmin.setFirstName("AYO");
        godAdmin.setLastName("Omope");
        godAdmin.setEmail("ayo@eduverse.com");
        godAdmin.setPassword("password");
        adminRepo.save(godAdmin);

    }


    public void createFeatures(FeatureRepo featureRepo, UniversityFeatureRepo universityFeatureRepo) {
        List<Feature> features = new ArrayList<>();

        Feature feature1 = new Feature();
        feature1.setName(FEATURE.CONTENT_POSTING);
        feature1.setDescription(FEATURE.getFeatureDescription(FEATURE.CONTENT_POSTING));
        feature1.setType(FEATURE_TYPE.CONTENT);
        features.add(feature1);


        Feature feature2 = new Feature();
        feature2.setName(FEATURE.CONTENT_FEED);
        feature2.setDescription(FEATURE.getFeatureDescription(FEATURE.CONTENT_FEED));
        feature2.setType(FEATURE_TYPE.CONTENT);
        features.add(feature2);

//        Feature feature3 = new Feature();
//        feature3.setFeatureName(FEATURE.PRIVATE_MESSAGING);
//        feature3.setFeatureDescription(FEATURE.getFeatureDescription(FEATURE.PRIVATE_MESSAGING));
//        features.add(feature3);

        Feature feature3 = new Feature();
        feature3.setName(FEATURE.MODULE);
        feature3.setType(FEATURE_TYPE.MODULE);
        feature3.setDescription(FEATURE.getFeatureDescription(FEATURE.PRIVATE_MESSAGING));
        features.add(feature3);

        Feature feature4 = new Feature();
        feature4.setName(FEATURE.GROUP);
        feature4.setType(FEATURE_TYPE.GROUP);
        feature4.setDescription(FEATURE.getFeatureDescription(FEATURE.GROUP));
        features.add(feature4);

        Feature feature5 = new Feature();
        feature5.setName(FEATURE.POST_COMMENTING);
        feature5.setType(FEATURE_TYPE.CONTENT);
        feature5.setDescription(FEATURE.getFeatureDescription(FEATURE.POST_COMMENTING));
        features.add(feature5);

        Feature feature6 = new Feature();
        feature6.setName(FEATURE.EVENTS);
        feature6.setType(FEATURE_TYPE.EVENT);
        feature6.setDescription(FEATURE.getFeatureDescription(FEATURE.EVENTS));
        features.add(feature6);

        featureRepo.saveAll(features);
    }


    public void createUsers(FeatureRepo featureRepo,
                            UniversityRepo universityRepo,
                            ProfileInfoRepo profileInfoRepo,
                            UserRepo userRepo,
                            DepartmentRepo departmentRepo,
                            CourseRepo courseRepo,
                            StudentRepo studentRepo,
                            StaffRepo staffRepo,
                            PasswordEncoder bCryptPasswordEncoder,
                            UniversityFeatureRepo universityFeatureRepo

    ) {

        University university1 = new University();
        university1.setName("London South Bank University");
        university1.setVerified(true);
        university1.setPhoneNumber("020 7815 7815");
        university1.setDomain("lsbu.ac.uk");
        university1.setLogoUrl("https://eduverse-v1.s3.eu-west-2.amazonaws.com/1/lsbu_logo.svg");
        university1.setPrimaryTheme("#000000");
        university1.setSecondaryTheme("#ffffff");
        university1 = universityRepo.save(university1);


        Map<FEATURE, Boolean> flags = new HashMap<>();

        List<Feature> features = featureRepo.findAll();
        List<UniversityFeature> universityFeatures = new ArrayList<>();


        for (Feature feature : features) {
            UniversityFeature universityFeature = new UniversityFeature();
            universityFeature.setFeature(feature);
            universityFeature.setAuthorizedUsers(AUTHORITY.STANDARD);
            universityFeature.setAuthorizedMembers(USER_TYPE.ALL);
            universityFeature.setEnabled(true);
            universityFeature.setUniversity(university1);
            universityFeatures.add(universityFeature);
        }

        universityFeatureRepo.saveAll(universityFeatures);

        Department department1 = new Department();
        department1.setDepartmentName("Engineering");
        department1.setDepartmentType(DEPARTMENT_TYPE.ACADEMIC);
        department1.setDepartmentCode("ENG");
        department1.setUniversity(university1);
        departmentRepo.save(department1);


        Course course1 = new Course();
        course1.setCourseName("BSc Computer Science");
        course1.setCourseCode("4703");
        course1.setDepartment(department1);
        courseRepo.save(course1);


        ProfileInfo profileInfo1 = new ProfileInfo();
        profileInfo1.setBio("I am a student at London South Bank University");
        profileInfo1.setProfilePicUrl("https://www.lsbu.ac.uk/");
        profileInfo1.setCoverPicUrl("https://www.lsbu.ac.uk/");


        Student student1 = new Student();
        student1.setCourse(course1);
        student1.setStudentNumber("345221");
        student1.setStartYear(2012);


        AppUser user1 = new AppUser();
        user1.setFirstName("john");
        user1.setLastName("doe");
        user1.setUsername("johndoe");
        user1.setAuthority(AUTHORITY.ADMIN);
        user1.setUserType(USER_TYPE.STUDENT);
        user1.setEmail("johndoe@lsbu.ac.uk");
        user1.setPassword(bCryptPasswordEncoder.encode("12345"));
        user1.setUniversity(university1);
        user1.setProfileInfo(profileInfo1);
        profileInfo1.setUser(user1);
        student1.setUser(user1);
        user1.setStudent(student1);

        userRepo.save(user1);


        ProfileInfo profileInfo2 = new ProfileInfo();
        profileInfo2.setBio("I am a student at London South Bank University");
        profileInfo2.setProfilePicUrl("https://eduverse-v1.s3.eu-west-2.amazonaws.com/1/lsbu_logo.svg");
        profileInfo2.setCoverPicUrl("https://www.lsbu.ac.uk/");


        Staff staff1 = new Staff();
        staff1.setDepartment(department1);
        staff1.setStaffNumber("345221");
        staff1.setStaffType(STAFF_TYPE.LECTURER);


        AppUser user2 = new AppUser();
        user2.setFirstName("janet");
        user2.setLastName("harper");
        user2.setUsername("janetharper");
        user2.setAuthority(AUTHORITY.ADMIN);
        user2.setUserType(USER_TYPE.STAFF);
        user2.setEmail("janeharper@lsbu.ac.uk");
        user2.setPassword(bCryptPasswordEncoder.encode("12345"));
        user2.setUniversity(university1);
        user2.setProfileInfo(profileInfo2);
        staff1.setUser(user2);
        profileInfo2.setUser(user2);
        user2.setStaff(staff1);

        userRepo.save(user2);


    }


}
