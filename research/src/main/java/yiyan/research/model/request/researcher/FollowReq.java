package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class FollowReq {
    private String userId;
    private String authorId;
}
