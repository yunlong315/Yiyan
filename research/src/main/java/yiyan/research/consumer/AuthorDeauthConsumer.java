package yiyan.research.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.entity.consumer.AuthorAuthReceive;
import yiyan.research.model.entity.consumer.AuthorDeauthReceive;
import yiyan.research.service.AuthorService;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "yes", topic = "ResearchTopic",selectorExpression = "AuthorDeauthenticate")
public class AuthorDeauthConsumer
        implements RocketMQReplyListener<AuthorDeauthReceive, Boolean> {
    @Resource
    AuthorService authorService;
    @Override
    public Boolean onMessage(AuthorDeauthReceive authorDeauthReceive) {
        log.info("接收到消息："+ authorDeauthReceive +"----"+System.currentTimeMillis());
        String userId = authorDeauthReceive.getUserId();
        String authorId= authorDeauthReceive.getAuthorId();
        Authors authors = authorService.getAuthorById(authorId);
        if(authors == null) return true;//没有这个author，自然解绑成功
        if(authors.getUserId()!=null && authors.getUserId()!=userId)return false;
        authorService.auth(authorId,"");
        return true;
    }
}
