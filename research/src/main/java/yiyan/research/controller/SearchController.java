package yiyan.research.controller;

import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiyan.research.model.esEntity.ESAuthor;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.model.request.search.AuthorSearchReq;
import yiyan.research.model.request.search.GetRelatedWorksReq;
import yiyan.research.model.request.search.InstSearchReq;
import yiyan.research.model.request.search.PaperSearchReq;
import yiyan.research.model.response.search.*;
import yiyan.research.service.*;

@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "*")
public class SearchController {
    @Resource
    SearchService searchService;
    @Resource
    WorkService workService;
//    @Resource
//    RocketMQService rocketMQService;
    @Resource
    FollowService followService;
    @Resource
    CacheService cacheService;

    @PostMapping("/paper")
    public ResponseEntity<PaperSearchRsp> searchWork(@RequestBody PaperSearchReq request, @RequestHeader(name = "token", required = false) String token){
        PaperSearchRsp response = cacheService.getObject(request, PaperSearchRsp.class);
        if(response == null){
            response = searchService.searchWorks(request);
            for (ESWork work : response.getResults()){
                work.setViewCount(workService.getWorksInfo(work.getId()).getViewCount());
//                if(token!=null){
//                    work.setLike(rocketMQService.getWorkIsLikeByUser(token, work.getId()));
//                }
            }
            cacheService.add(request, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/institution")
    public ResponseEntity<InstSearchRsp> searchInst(@RequestBody InstSearchReq request){
        InstSearchRsp response = cacheService.getObject(request, InstSearchRsp.class);
        if(response == null){
            response = searchService.searchInst(request);
            cacheService.add(request, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/author")
    public ResponseEntity<AuthorSearchRsp> searchAuthor(@RequestBody AuthorSearchReq request, @RequestHeader(name = "token", required = false) String token){
        AuthorSearchRsp response = cacheService.getObject(request, AuthorSearchRsp.class);
        if(response == null){
            response = searchService.searchAuthor(request);
//            for(ESAuthor author : response.getResults()){
//                if(token!=null){
//                    author.setFollowing(followService.isFollow(rocketMQService.getUserId(token), author.getId()));
//                }
//            }
            cacheService.add(request, response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/relatedWorks")
    public ResponseEntity<GetRelatedWorksRsp> getRelatedWorks(@RequestBody GetRelatedWorksReq request){
        GetRelatedWorksRsp response = cacheService.getObject(request, GetRelatedWorksRsp.class);
        if(response == null){
            response = searchService.getRelatedWorks(request);
            cacheService.add(request, response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hotPapers")
    public ResponseEntity<HotPaperRsp> getHotPapers(){
        return ResponseEntity.ok(searchService.getHotWorks());
    }
}
