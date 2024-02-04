package yiyan.research.consumer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;
import yiyan.research.mapper.WorkDetailMapper;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.*;
import yiyan.research.service.AuthorService;
import yiyan.research.service.WorkDetailService;
import yiyan.research.service.WorkService;

import java.net.URL;
import java.util.*;

@Slf4j
@Component
@RocketMQMessageListener(topic = "ResearchTopic", consumerGroup = "researchConsumer")
public class ResearchListener implements RocketMQReplyListener<Map, Map> {

    @Resource
    private AuthorService authorService;
    @Resource
    private WorkService workService;
    @Resource
    private WorkDetailMapper workDetailMapper;
    @Override
    public Map onMessage(Map m) {
        int type=(int)m.get("type");
        Map<String,Object> map=new HashMap<>();
        System.out.println("接收到消息：type: "+ type +"----"+System.currentTimeMillis());
        switch (type) {
            case 1 -> {
                //String token = (String) m.get("token");
                //int id=manageService.getIdByToken(token);
                //map.put("id", String.valueOf(id));
                String userId = (String) m.get("userId");
                String authorId = (String) m.get("authorId");
                Authors authors = authorService.getAuthorById(authorId);
                if (authors == null) {
                    map.put("success", false);
                    return map;
                }
                if (authors.getUserId() == null || authors.getUserId().equals("")) {
                    authorService.auth(authorId, userId);
                    map.put("success", true);
                    return map;
                }
                map.put("success", false);
                return map;
            }
            case 2 -> {
                String userId = (String) m.get("userId");
                String authorId = (String) m.get("authorId");
                Authors authors = authorService.getAuthorById(authorId);
                if (authors == null) {
                    map.put("success", true);
                    return map;
                }//没有这个author，自然解绑成功
                if (authors.getUserId() != null && authors.getUserId() != userId) {
                    map.put("success", false);
                    return map;
                }
                authorService.auth(authorId, "");
                map.put("success", false);
                return map;
            }
            case 3 -> {
                String authorId = (String) m.get("authorId");
                String workId = (String) m.get("workId");
                workService.deleteWorkAuthorship(authorId,workId);
                authorService.reduceWorksCount(authorId);
                map.put("success", true);
                return map;
            }
            case 4 -> {
                Map<String, Object> a = new HashMap<>();
                if (!m.containsKey("workId")) {
                    System.out.println("不含workId");
                    a.put("success", false);
                    break;
                }
                String workId = (String) m.get("workId");
                Works works = workDetailMapper.getInfo(workId);
                if(works==null){
                    System.out.println("works为空workId :"+workId);
                    a.put("success", false);
                    break;
                }


                a.put("id", workId);
                a.put("title", works.getTitle());
                WorksAuthorships[] worksAuthorships = workDetailMapper.getWorksAuthorships(workId);
                List<Map<String, String>> author = new ArrayList<>();
                for (WorksAuthorships w : worksAuthorships) {
                    String authorId = w.getAuthorId();
                    String name = workDetailMapper.getAuthorName(authorId);
                    Map<String, String> aa = new HashMap<>();
                    aa.put("id", authorId);
                    aa.put("name", name);
                    author.add(aa);
                }
                a.put("authors", author);
                a.put("year", works.getPublicationYear());
                a.put("quoteCnt", works.getCitedByCount());
                WorksInfo info = workDetailMapper.getWorkInfo(workId);
                a.put("browseCnt", info != null ? info.getViewCount() : 0);
                a.put("commentCnt", workDetailMapper.getAllComment(workId).length);
                a.put("success", true);
                return a;
            }
            default -> {
                map.put("success", false);
            }
        }
        return map;
    }
}
