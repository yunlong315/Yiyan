package yiyan.research.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateInfoReq {
    private String id;
    private String displayInstitution;
    private String email;
    private String introduction;
    private String homepage;
    private String googleScholar;
    private String professionalTitle;
    private String education;
    private String experience;
    private MultipartFile avatar;//认证用户头像
}
