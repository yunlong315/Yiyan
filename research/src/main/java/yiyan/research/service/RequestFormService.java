package yiyan.research.service;

import org.springframework.stereotype.Service;

@Service
public interface RequestFormService {
    int addApplyForm(String userId, String authorId, String content, String attachmentUrl);
}
