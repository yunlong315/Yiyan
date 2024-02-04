package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.RequestFormMapper;
import yiyan.research.model.domain.RequestForm;
import yiyan.research.service.RequestFormService;

import java.util.Date;

@Service
public class RequestFormServiceImp implements RequestFormService {
    @Resource
    private RequestFormMapper requestFormMapper;
    @Override
    public int addApplyForm(String userId, String authorId, String content, String attachmentUrl) {
        RequestForm requestForm = new RequestForm(1, userId, content, authorId, attachmentUrl, new Date());
        return requestFormMapper.insertRequestForm(requestForm);
    }
}
