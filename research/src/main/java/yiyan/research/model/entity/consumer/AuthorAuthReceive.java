package yiyan.research.model.entity.consumer;

import lombok.Data;

@Data
public class AuthorAuthReceive {
    private String userId;
    private String authorId;
    public AuthorAuthReceive(String userId,String authorId){
        this.userId = userId;
        this.authorId = authorId;
    }
}
