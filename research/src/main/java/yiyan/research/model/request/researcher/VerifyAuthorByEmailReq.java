package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class VerifyAuthorByEmailReq {
    private String userId;
    private String authorId;
}
