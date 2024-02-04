package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class UnfollowReq {
    private String userId;
    private String authorId;
}
