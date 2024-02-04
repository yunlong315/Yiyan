package yiyan.research.service;

import org.springframework.web.multipart.MultipartFile;

public abstract class COSService {
    public abstract String uploadFile(MultipartFile file, String path);

    public abstract String uploadAuthorAvatar(MultipartFile file, String path);
}
