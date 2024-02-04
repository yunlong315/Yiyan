package yiyan.research.controller;

import yiyan.research.mapper.WorkMapper;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.request.researcher.GetWorkListByEntryReq;
import yiyan.research.service.CacheService;
import yiyan.research.service.RocketMQService;
import yiyan.research.service.TestService;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource
    private TestService testService;
    @Resource
    private CacheService cacheService;

    @Resource
    private WorkMapper workMapper;
    @Resource
    private RocketMQService rocketMQService;

    @PostMapping("/temSend")
    public String send(@RequestParam String message){
        //rocketMQTemplate.convertAndSend("Test-Template-Topic",message+Math.random());
        rocketMQService.authorAuthenticate("1","A1");
        return "ok";
    }
    @PostMapping("/testCache")
    public String testCache(@RequestParam String message){
        if(cacheService.get("1")==null){
            cacheService.add("1",message);
        }else{
            System.out.println("cache已有了");
            if( cacheService.get("1")!=message){
                cacheService.add("1",message);
            }
            return cacheService.get("1");
        }
        return "ok";
    }
    @PostMapping("/testGetCache")
    public String testGetCache(@RequestParam String message){
        return cacheService.get("1");
    }
    @GetMapping("/hello")
    public ResponseEntity<Authors> hello() {
        Authors author = new Authors();
        author.setId(String.valueOf(333));
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
    @GetMapping("/test")
    public String test(@RequestParam int id) {
        return null;
    }
    @PostMapping("/testSQL")
    public ResponseEntity testSQL(@RequestBody GetWorkListByEntryReq getWorkListByEntryReq){
        String authorId = getWorkListByEntryReq.getAuthorId();
        String[] concepts = getWorkListByEntryReq.getConceptIds();
        return ResponseEntity.ok(workMapper.getWorksByAuthorIdAndConcepts(authorId,concepts));
    }

    @PostMapping("/testAuth")
    public boolean  authorAuthenticate(String userId,String authorId) {
        Map<String, Object> map = new HashMap<>() {{
            put("type", 1);
            put("userId", userId);
            put("authorId", authorId);
        }};
        Map<String, Object> o = rocketMQTemplate.sendAndReceive("ResearchTopic", map, Map.class);
        //System.out.println(o == null);
        return (boolean) o.get("success");
    }
}
