package yiyan.research.model.request;

import lombok.Data;

@Data
public class UpdatePermissionRequest {
    private String workId;
    private String authorId;
}
