package yiyan.research.model.domain;

import lombok.Data;

@Data
public class WorksInfo {
    private String workId;
    private boolean isCommentAllowed=true;
    private int viewCount = 0;
    private int collectionCount = 0;
    private String attachmentUrl;
    public WorksInfo(String id){
        workId = id;
    }
}
