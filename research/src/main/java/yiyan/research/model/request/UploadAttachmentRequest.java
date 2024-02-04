package yiyan.research.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@Data
public class UploadAttachmentRequest {
    String workId;
    MultipartFile attachment;
}
