package dev.captain.postservice.repository;


import dev.captain.postservice.model.Enums.POST_ACCESS;
import dev.captain.postservice.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepo extends MongoRepository<Post, String> {


    Page<Post> findAllByAccessAndUniversityId(PageRequest p, POST_ACCESS postAccess, Long universityId);


    Page<Post> findByUserIdInAndAccess(PageRequest of, List<Long> friends, POST_ACCESS postAccess);



    Page<Post> findAllByUserIdAndFacultyIdIsNull(PageRequest p, Long userId);
}
