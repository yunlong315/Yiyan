package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.Follow;
import yiyan.research.model.domain.openalex.Authors;

import java.util.List;

@Service
public interface FollowService {
    int follow(String userId, String authorId);
    boolean isFollow(String userId, String authorId);
    int unfollow(String id, String authorId);

    List<Follow> getFollowList(String userId);
}
