package yiyan.research.model.entity;

import lombok.Data;

@Data
public class FollowedAuthor {
    private String authorId;
    private String avatarUrl;
    private String authorName;
    private String email;
    private String insName;
    private boolean isFollowing = true;
}
