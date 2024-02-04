package yiyan.research.model.request;


import lombok.Data;

@Data
public class CommentRequest {
    //    private int userId;
    private String content;
    private int parentId;
    private String workId;
}
