package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yiyan.research.model.entity.consumer.AuthorAuthReceive;
import yiyan.research.model.producer.Producer;
import yiyan.research.service.RocketMQService;

import java.util.HashMap;
import java.util.Map;

@Service
public class RocketMQServiceImp implements RocketMQService {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Resource
    private Producer producer;
    @Override
    public String getUserId(String token) {
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 1);
            put("token", token);
        }});
        return (String) o.get("id");
    }

    @Override
    public boolean getUserIsAdmin(String token) {
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 2);
            put("token", token);
        }});
        return (Boolean) o.get("isAdmin");
    }

    @Override
    public boolean getWorkIsLikeByUser(String token, String workId){
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 3);
            put("token", token);
        }});
        return (Boolean) o.get("isFavorite");
    }
    @Override
    public boolean sendAuthorApplication(String token, String email){
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 5);
            put("token", token);
            put("email", email);
        }});
        System.out.println(o.get("success"));
        return (Boolean) o.get("success");
    }
    @Override
    public boolean authCode(String token, String email, String code){
        Map<String, Object> o = producer.syncSend(new HashMap<>() {{
            put("type", 6);
            put("token", token);
            put("email", email);
            put("code", code);
        }});
        return (Boolean) o.get("success");
    }
    @Override
    public boolean  authorAuthenticate(String userId, String authorId){
        AuthorAuthReceive authorAuthReceive = new AuthorAuthReceive(userId,authorId);
        boolean res = rocketMQTemplate.sendAndReceive("ResearchTopic:AuthorAuthenticate",authorAuthReceive,AuthorAuthReceive.class);
        return res;
    }
}
