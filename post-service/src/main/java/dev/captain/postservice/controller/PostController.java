package dev.captain.postservice.controller;


import dev.captain.postservice.model.Post;
import dev.captain.postservice.model.template.User;
import dev.captain.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post-service/")
@RequiredArgsConstructor
public class PostController {

    public final PostService postService;
    public final RestTemplate RestTemplate;


    @PostMapping("posts")
    public ResponseEntity<?> createPost(@RequestBody Post newPost) {

        if (newPost == null) {
            return ResponseEntity.badRequest().body("Post cannot be empty");
        }
        if (newPost.getUserId() == null || newPost.getUniversityId() == null) {
            return ResponseEntity.badRequest().body("Please provide a user id & university id");
        }

        String fileName = newPost.getImageUrl();
        if (fileName != null) {
            String awsFileName = "https://eduverse-v1.s3.eu-west-2.amazonaws.com/" + newPost.getUniversityId() + "/" + fileName;
            newPost.setImageUrl(awsFileName);
        }

        newPost.setCreatedAt(LocalDateTime.now());
        Post savedPost = postService.createPost(newPost);
        return ResponseEntity.ok(savedPost);
    }

    @PostMapping("posts/{postId}/likes/{isLiked}")
    public ResponseEntity<?> likePost(@PathVariable("postId") String postId, @RequestParam Long userId, @PathVariable boolean isLiked) {
        postService.likePost(postId, userId, isLiked);
        return ResponseEntity.ok("Post liked");
    }


    @GetMapping("university/{universityId}/posts/public")
    public ResponseEntity<?> getPublicPosts(@RequestParam Optional<Integer> page,
                                            @RequestParam Optional<String> sortBy,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam Optional<Long> size,
                                            @PathVariable("universityId") Long universityId) {

        return ResponseEntity.ok(postService.getPublicPosts(PageRequest.of(page.orElse(0), size.orElse(20L).intValue(),
                Sort.Direction.valueOf(sort.orElse("DESC")), sortBy.orElse("createdAt")), universityId));
    }


    @GetMapping("users/{user-id}/posts/following")
    public ResponseEntity<?> getFriendPosts(@RequestParam Optional<Integer> page,
                                            @RequestParam Optional<String> sortBy,
                                            @RequestParam Optional<String> sort,
                                            @RequestParam Optional<Long> size,
                                            @PathVariable("user-id") Long userId) {


        List <User> following = null;

        try {
            following = RestTemplate.exchange("http://USER-SERVICE/api/user-service/relationship/following?userId=" +
                    userId, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
            }).getBody();
        } catch (Exception ignored) {
            System.out.println("Error in getting following");
        }

        if(following == null || following.isEmpty()) {
            return ResponseEntity.badRequest().body("No following found");
        }

        List<Long> followingIds = following.stream().map(User::getId).toList();
        System.out.println(followingIds);

        Page<Post> posts = postService.getFollowingPosts(PageRequest.of(page.orElse(0), size.orElse(20L).intValue(),
                Sort.Direction.DESC, sortBy.orElse("createdAt")), followingIds);

        System.out.println(posts.getContent());

        if (posts.isEmpty()) {
            return ResponseEntity.badRequest().body("No posts from friends");
        }

        return ResponseEntity.ok(posts);
    }

    @GetMapping("posts/{post-id}")
    public ResponseEntity<?> getPost(@PathVariable("post-id") String postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }


    @GetMapping("/users/{user-id}/posts")
    public ResponseEntity<?> getUserPosts(@RequestParam Optional<Integer> page,
                                          @RequestParam Optional<String> sortBy,
                                          @RequestParam Optional<String> sort,
                                          @RequestParam Optional<Long> size,
                                          @PathVariable("user-id") Long userId) {

        Page<Post> posts = postService.getPostsByUserIdWithoutFaculty(PageRequest.of(page.orElse(0), size.orElse(20L).intValue(),
                Sort.Direction.DESC, sortBy.orElse("createdAt")), userId);

        if (posts.isEmpty()) {
            return ResponseEntity.badRequest().body("No posts from user");
        }

        return ResponseEntity.ok(posts);
    }


}
