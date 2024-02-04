package yiyan.research.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.entity.consumer.AuthorAuthReceive;
import yiyan.research.service.AuthorService;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "yes", topic = "ResearchTopic",selectorExpression = "AuthorAuthenticate")
public class AuthorAuthConsumer
        implements RocketMQReplyListener<AuthorAuthReceive, Boolean> {
    @Resource
    private AuthorService authorService;
    @Override
    public Boolean onMessage(AuthorAuthReceive authorAuthReceive) {
        log.info("接收到消息："+ authorAuthReceive +"----"+System.currentTimeMillis());
        String userId = authorAuthReceive.getUserId();
        String authorId= authorAuthReceive.getAuthorId();
        Authors authors = authorService.getAuthorById(authorId);
        if(authors == null) return false;
        if(authors.getUserId()==null||authors.getUserId().equals("")){
            authorService.auth(authorId,userId);
            return true;
        }
        return false;
    }
}
