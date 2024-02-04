package yiyan.research.consumer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import yiyan.research.model.entity.consumer.UnclaimWorkReceive;
import yiyan.research.service.AuthorService;
import yiyan.research.service.WorkService;

@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "yes", topic = "ResearchTopic",selectorExpression = "UnclaimWork")
public class UnclaimWorkConsumer
        implements RocketMQReplyListener<UnclaimWorkReceive, Boolean> {
    @Resource
    WorkService workService;
    @Resource
    AuthorService authorService;
    @Override
    public Boolean onMessage(UnclaimWorkReceive rece) {
        log.info("接收到消息："+ rece +"----"+System.currentTimeMillis());
        String authorId= rece.getAuthorId();
        String workId= rece.getWorkId();
        workService.deleteWorkAuthorship(authorId,workId);
        authorService.reduceWorksCount(authorId);
        return true;
    }
}
