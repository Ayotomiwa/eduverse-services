package dev.captain.postservice.service;

import dev.captain.postservice.model.Enums.POST_ACCESS;
import dev.captain.postservice.model.Post;
import dev.captain.postservice.repository.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    public final PostRepo postRepo;


    public Page<Post> getPublicPosts(PageRequest p, Long universityId) {
        return postRepo.findAllByAccessAndUniversityId(p, POST_ACCESS.PUBLIC, universityId);
    }

    public Page<Post> getFollowingPosts(PageRequest p, List<Long> followedUsers) {
        return postRepo.findByUserIdInAndAccess(p, followedUsers, POST_ACCESS.STANDARD);
    }

    public Post getPost(String postId) {
        return postRepo.findById(postId).orElse(null);
    }

    public boolean postExists(String postId) {
        return postRepo.existsById(postId);
    }

    public Post createPost(Post post) {
        return postRepo.save(post);
    }

    public void likePost(String postId, Long userId, boolean like) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post != null) {
            List<Long> likesIds = post.getLikesIds();
            if (likesIds == null) {
                if (!like) {
                    return;
                }
                likesIds = new ArrayList<>();
                likesIds.add(userId);
                post.setLikesIds(likesIds);
            } else {
                if (likesIds.contains(userId)) {
                    if (!like) {
                        likesIds.remove(userId);
                        post.setLikesIds(likesIds);
                        postRepo.save(post);
                    }
                    return;
                }
                likesIds.add(userId);
                post.setLikesIds(likesIds);
            }
            postRepo.save(post);
        }
    }

    public Page<Post> getPostsByUserIdWithoutFaculty(PageRequest p, Long userId) {
        return postRepo.findAllByUserIdAndFacultyIdIsNull(p, userId);
    }

    public void deletePost(String postId) {
        postRepo.deleteById(postId);
    }
}
