package dev.captain.groupservice;

import com.github.javafaker.Faker;
import dev.captain.groupservice.model.Comment;
import dev.captain.groupservice.model.Discussion;
import dev.captain.groupservice.model.Group;
import dev.captain.groupservice.model.enums.GROUP_CATEGORY;
import dev.captain.groupservice.model.enums.GROUP_TYPE;
import dev.captain.groupservice.repository.CommentRepo;
import dev.captain.groupservice.repository.DiscussionRepo;
import dev.captain.groupservice.service.GroupService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class GroupServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroupServiceApplication.class, args);
    }


    @Bean
    public CommandLineRunner create(
            MongoTemplate mongoTemplate,
            GroupService groupService,
            DiscussionRepo discussionRepo,
            CommentRepo commentRepo

    ) {
        return (args) -> {
//            mongoTemplate.dropCollection("userGroup");
//            mongoTemplate.dropCollection("group");
//            mongoTemplate.dropCollection("discussion");
//            mongoTemplate.dropCollection("comment");


            GROUP_TYPE[] groupTypes = GROUP_TYPE.values();
            List<Group> groups = new ArrayList<>();


            Faker faker = new Faker();
            for (int i = 1; i < 3; i++) {
                Group newGroup = new Group();
                newGroup.setName(faker.team().name());
                newGroup.setDescription(faker.lorem().sentence());
                newGroup.setProfilePicUrl(faker.internet().image());
                newGroup.setAbout(faker.lorem().paragraph());
                newGroup.setCreatorId((long) i);
                newGroup.setApproved(true);
                newGroup.setBlocked(false);
                newGroup.setCreatorUsername("johnDoe");
                newGroup.setType(groupTypes[faker.random().nextInt(groupTypes.length)]);
                newGroup.setCategory(GROUP_CATEGORY.CAREER);
                groupService.createGroup(newGroup, 1L);
                groups.add(newGroup);
            }

            List<Discussion> list = new ArrayList<>();
            long count = 1L;

            for (int i = 0; i < 10; i++) {
                Discussion discussion = new Discussion();
                discussion.setGroupId(groups.get(0).getId());
                discussion.setTopic(faker.lorem().paragraph());
                discussion.setLikesIds(Arrays.asList(1L, 2L, 3L));
                discussion.setUserId((new Random().nextLong(1, 3)));
                discussion.setUsername(faker.name().username());
                discussion.setCount(count++);
                discussionRepo.save(discussion);
                list.add(discussion);
            }
            List<Comment> comments = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                Comment newComment = new Comment();
                newComment.setDiscussionId(list.get(0).getId());
                newComment.setUsername(faker.name().username());
                newComment.setUserId(new Random().nextLong(1, 3));
                newComment.setLikesIds(Arrays.asList(1L, 2L, 3L));
                newComment.setCreatedAt(LocalDateTime.now());
                newComment.setComment(faker.lorem().sentence());
                comments.add(newComment);
                commentRepo.save(newComment);
            }

            for (int i = 0; i < 10; i++) {
                Comment newComment = new Comment();
                newComment.setDiscussionId(list.get(0).getId());
                newComment.setUsername(faker.name().username());
                newComment.setParentComment(comments.get(new Random().nextInt(1, 10)));
                newComment.setComment(faker.lorem().sentence());
                newComment.setUserId(new Random().nextLong(1, 3));
                newComment.setCreatedAt(LocalDateTime.now());
                newComment.setLikesIds(Arrays.asList(1L, 2L, 3L));
                commentRepo.save(newComment);
            }


        };
    }


}
