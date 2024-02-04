package yiyan.research.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DeleteCommentRequest {
    public int commentId;
    MultipartFile avatarFile;
}