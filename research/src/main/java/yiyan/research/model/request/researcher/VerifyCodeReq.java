package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class VerifyCodeReq {
    private String code;
    private String authorId;
}
