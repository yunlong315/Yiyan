package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.Message;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

@Service
public abstract class WorkDetailService  {
    public abstract Map<String, Object> getInfo(String workId,String token) throws MalformedURLException;
    public abstract boolean comment(int userId,String content,int parentId,String workId);
    public abstract boolean deleteComment(int userId,int commentId,boolean isAdmin);
    public abstract List<Map<String, Object>> getAllComment(String workId);
//    public abstract Message getChat(List<Message> messages);
    public abstract boolean updateCommentPermission(String workId,String authorId);
    public abstract boolean getCommentPermission(String workId);
    public abstract void updateAttachment(String workId,String url);
    public abstract Map<String, Object> getCite(String workId);
    public abstract boolean getPermission(String workId,String authorId);
}
