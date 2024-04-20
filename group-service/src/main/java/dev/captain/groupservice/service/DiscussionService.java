package dev.captain.groupservice.service;


import dev.captain.groupservice.model.Discussion;
import dev.captain.groupservice.repository.DiscussionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscussionService {
    private final DiscussionRepo discussionRepo;


    public Page<Discussion> getDiscussions(String groupId, PageRequest p) {
        return discussionRepo.findAllByGroupId(groupId, p);

    }

    public Discussion create(Discussion discussion) {
        return discussionRepo.save(discussion);
    }

    public Discussion getDiscussion(String threadId) {
        return discussionRepo.findById(threadId).orElse(null);
    }

    public void likeDiscussion(String discussionId, Long userId, boolean isLiked) {
        Discussion discussion = discussionRepo.findById(discussionId).orElse(null);
        if (discussion != null) {
            List<Long> likesIds = discussion.getLikesIds();
            if (likesIds == null) {
                if (!isLiked) {
                    return;
                }
                likesIds = List.of(userId);
                discussion.setLikesIds(likesIds);
            } else {
                if (likesIds.contains(userId)) {
                    if (!isLiked) {
                        likesIds.remove(userId);
                        discussion.setLikesIds(likesIds);
                        discussionRepo.save(discussion);
                    }
                } else {
                    if (isLiked) {
                        likesIds.add(userId);
                        discussion.setLikesIds(likesIds);
                        discussionRepo.save(discussion);
                    }
                }
            }
        }
    }
}
