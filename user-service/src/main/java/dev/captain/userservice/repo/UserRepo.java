package dev.captain.userservice.repo;

import dev.captain.userservice.model.enums.USER_TYPE;
import dev.captain.userservice.model.tables.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<AppUser, Long> {


    Optional<AppUser> findByEmail(String email);

    Page<AppUser> findAllByUniversityId(PageRequest pageable, Long universityId);


    boolean existsAppUserByEmail(String email);

    boolean existsAppUserById(Long userId);

    AppUser findAppUserById(Long userId);

    List<AppUser> findAllByUniversityId(Long universityId);


    Page<AppUser> findAllByUserTypeAndUniversityId(PageRequest id, USER_TYPE userType, Long universityId);


    List<AppUser> findByUniversityIdAndFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLikeOrEmailIgnoreCaseLike
            (Long universityId, String queryParameter, String queryParameter1, String queryParameter2);


    Page<AppUser> findAllByUniversityIdAndUserTypeOrUserType(PageRequest page, Long universityId, USER_TYPE userType, USER_TYPE userType1);


    List<AppUser> findByUniversityIdAndUserTypeInAndFirstNameIgnoreCaseLikeOrLastNameIgnoreCaseLikeOrEmailIgnoreCaseLike
            (Long university_id, List<USER_TYPE> userType, String firstName, String lastName, String email);


    List<AppUser> findByUniversityIdAndUserType(Long universityId, USER_TYPE userType);


    @Query("SELECT u FROM AppUser u WHERE u.university.id = :universityId AND (upper(u.firstName) LIKE upper(:name) OR upper(u.lastName) LIKE upper(:name) OR upper(u.email) LIKE upper(:email))")
    List<AppUser> findByUniversityIdAndNameOrEmail(@Param("universityId") Long universityId, @Param("name") String name,  @Param("email") String queryParameter1);


    @Query("SELECT u FROM AppUser u WHERE u.university.id = :universityId AND (u.userType = :userType1 OR u.userType = :userType2) AND (upper(u.firstName) LIKE upper(:name) OR upper(u.lastName) LIKE upper(:name) OR upper(u.email) LIKE upper(:email))")
    List<AppUser> findByUniversityIdAndStudentOrAlumniAndNameOrEmail(@Param("universityId") Long universityId, @Param("userType1") USER_TYPE userType1, @Param("userType2") USER_TYPE userType2, @Param("name") String name, @Param("email") String email);




    @Query("SELECT u FROM AppUser u WHERE u.university.id = :universityId AND u.userType = :userType AND (upper(u.firstName) LIKE upper(:name) OR upper(u.lastName) LIKE upper(:name) OR upper(u.email) LIKE upper(:email))")
    List<AppUser> findByUniversityIdAndStaffAndNameOrEmail(@Param("universityId") Long universityId, @Param("userType") USER_TYPE userType, @Param("name") String name, @Param("email") String email);
}

