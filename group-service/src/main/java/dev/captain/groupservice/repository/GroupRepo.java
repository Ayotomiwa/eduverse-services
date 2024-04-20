package dev.captain.groupservice.repository;

import dev.captain.groupservice.model.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepo extends MongoRepository<Group, String> {


    List<Group> findAllByUniversityId(Long universityId);

    List<Group> findAllByIdIn(List<String> groupIds);

    List<Group> findByNameRegexAndUniversityId(String query, Long universityId);

    List<Group> findByNameRegexAndIdIn(String query, List<String> groupIds);
}

   

