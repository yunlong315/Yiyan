package com.example.backendusermanagement.consumer;

import com.example.backendusermanagement.service.EmailService;
import com.example.backendusermanagement.service.FavoriteService;
import com.example.backendusermanagement.service.ManageService;
import com.example.backendusermanagement.service.MessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import com.example.backendusermanagement.model.domain.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RocketMQMessageListener(topic = "userTopic", consumerGroup = "userConsumer")
public class msgListener implements RocketMQReplyListener<Map, Map> {
    @Resource
    ManageService manageService;
    @Resource
    MessageService messageService;
    @Resource
    FavoriteService favoriteService;
    @Resource
    EmailService emailService;

    @Override
    public Map onMessage(Map m) {
        Map<String, Object> map = new HashMap<>();
        try {
            int type;
            if (!m.containsKey("type")) {
                type = -1;
            } else
                type = (int) m.get("type");
            System.out.println("md type:"+type);
            switch (type) {
                case 1:
                    if (!(m.containsKey("token"))) {
                        map.put("success", "未传入token");
                        break;
                    }
                    String token = (String) m.get("token");
                    if(token.equals("")){
                        map.put("success", "未传入token");
                        break;
                    }
                    int id = manageService.getIdByToken(token);
                    map.put("id", id);
                    map.put("success", true);
                    break;
                case 2:
                    if (!m.containsKey("token")) {
                        map.put("success", "未传入token");
                        break;
                    }
                    token = (String) m.get("token");
                    id = manageService.getIdByToken(token);
                    User u = manageService.getUserById(String.valueOf(id));
                    map.put("id", id);
                    map.put("isAdmin", u.isAdmin());
                    map.put("success", true);
                    break;
                case 3:
                    if (!(m.containsKey("srcId") && m.containsKey("dstId") && m.containsKey("messageType") && m.containsKey("content"))) {
                        map.put("success", "未传入srcId、dstId、messageType、content某几个");
                        break;
                    }
                    int srcId = (int) m.get("srcId");//这是来源id，可以是用户或者系统
                    int dstId = (int) m.get("dstId");//消息接收方
                    int messageType = (int) m.get("messageType");

                /*
               messageType:
                1:投诉结果
                2；被评论通知
                3：被回复通知
                4：被点赞通知
                5:认证用户
                 */
                    String content = (String) m.get("content");
                    messageService.createMessage(messageType, srcId, dstId, content);
                    if(messageType==5){
                        String authorId= (String) m.get("authorId");
                        manageService.authenticate(dstId,authorId);
                    }
                    map.put("success", true);
                    break;
                case 4:
                    if (!(m.containsKey("token") && m.containsKey("workId"))) {
                        map.put("success", "未传入token或workId");
                        break;
                    }
                    token = (String) m.get("token");
                    String workId = (String) m.get("workId");
                    id = manageService.getIdByToken(token);
                    map.put("isFavorite", favoriteService.isFavorite(id, workId));
                    map.put("userId", id);
                    map.put("success", true);
                    break;
                case 5:
                    if (!(m.containsKey("token") && m.containsKey("email"))) {
                        map.put("success",false);
                        break;
                    }
                    System.out.println("开始发邮件");
                    token= (String) m.get("token");
                    String email = (String) m.get("email");
                    id = manageService.getIdByToken(token);
                    u = manageService.getUserById(String.valueOf(id));
                    int code=manageService.generateVerificationCode();
                    Map<String, String> model = new HashMap<>();
                    model.put("username", u.getUsername());
                    model.put("code", String.valueOf(code));
                    model.put("type", "2");
                    model.put("userId", String.valueOf(u.getId()));
                    model.put("email", email);
                    boolean emailSent = emailService.sendEmail1(email, "认领账户", "changePswd", model);
                    if(emailSent){
                        manageService.saveCode(model);
                        map.put("success", true);
                    }
                    else
                        map.put("success",false);
                    break;
                case 6:
                    if (!(m.containsKey("token") && m.containsKey("email")&&m.containsKey("code"))) {
                        map.put("success", false);
                        break;
                    }
                    token= (String) m.get("token");
                    id=manageService.getIdByToken(token);
                    email = (String) m.get("email");
                    code=(int)m.get("code");
                    boolean res=manageService.getCode(id,code,email);
                    map.put("success", res);
                    break;
                case 7:// token type,
                    if (!m.containsKey("token")) {
                        map.put("success", "未传入token");
                        break;
                    }
                    token = (String) m.get("token");
                    id = manageService.getIdByToken(token);
                    u = manageService.getUserById(String.valueOf(id));
                    map.put("authorId",u.getAuthorId());
                default:
                    map.put("success", "未传入type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }
}
