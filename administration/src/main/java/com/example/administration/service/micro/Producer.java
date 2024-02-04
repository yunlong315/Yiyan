package com.example.administration.service.micro;

import com.example.administration.model.micro.AuthorAuthReceive;
import com.example.administration.model.micro.AuthorDeauthReceive;
import com.example.administration.model.micro.CheckAdminReply;
import com.example.administration.model.micro.UnclaimWorkReceive;
import jakarta.annotation.Resource;
import org.apache.ibatis.javassist.compiler.ast.NewExpr;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class Producer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    //同步方法
    public int getUserId(String token) {
        Map<String,Object> map=new HashMap<>(){{put("token",token);put("type",1);}};
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("userTopic",map,Map.class);
        return  (Integer) o.get("id");
    }
    public CheckAdminReply checkAdmin(String token){
        Map<String,Object> map=new HashMap<>(){{put("token",token);put("type",2);}};
        System.out.println(map);
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("userTopic",map,Map.class);
        return new CheckAdminReply((Integer) o.get("id"),(Boolean) o.get("isAdmin"));
    }


    public boolean  authorAuthenticate(String userId,String authorId){
        Map<String,Object> map=new HashMap<>(){{put("type",1);put("userId",userId);put("authorId",authorId);}};
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("ResearchTopic",map,Map.class);
        System.out.println("receive res "+o);
        return (boolean) o.get("success");
    }

    //topic = "ResearchTopic",selectorExpression = "AuthorDeauthenticate")
    public boolean deAuthorAuthenticate(String userId,String  authorId){
        Map<String,Object> map=new HashMap<>(){{put("type",2);put("userId",userId);put("authorId",authorId);}};
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("ResearchTopic",map,Map.class);
        return (boolean) o.get("success");
    }
    //topic = "ResearchTopic",selectorExpression = "UnclaimWork")
    public boolean unClaimWork(String authorId,String workId){
        Map<String,Object> map=new HashMap<>(){{put("type",3);put("authorId",authorId);put("workId",workId);}};
        Map<String,Object> o  = rocketMQTemplate.sendAndReceive("ResearchTopic",map,Map.class);
        return (boolean) o.get("success");
    }

//    传入srcId，dstId，messageTYpe，content。
//    表示srcId给dstId发消息，内容为content。
//    其中messageType定义如下
// 1:投诉结果  2；被评论通知  3：被回复通知  4：被点赞通知 5:认证通过通知
    public boolean sendMessage(int srcId,int dstId,String content,int messageType){
        Map map =  Map.of(
                "srcId",srcId,
                "dstId",dstId,
                "messageType",messageType,
                "content",content,
                "type",3
        );
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("userTopic",map,Map.class);
        boolean res = (Boolean) o.get("success");
        return res;
    }
    //发送认证通知
    public boolean sendAuthMessage(int srcId,int dstId,String authorId,String content){
        int messageType = 5;
        Map map =  Map.of(
                "srcId",srcId,
                "dstId",dstId,
                "authoId",authorId,
                "messageType",messageType,
                "content",content,
                "type",3
        );
        Map<String,Object> o = rocketMQTemplate.sendAndReceive("userTopic",map,Map.class);
        boolean res = (Boolean) o.get("success");
        return res;
    }


}
