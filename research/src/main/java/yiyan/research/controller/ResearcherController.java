package yiyan.research.controller;

import co.elastic.clients.elasticsearch.indices.SettingsSimilarityScriptedTfidf;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.bouncycastle.asn1.cms.ecc.ECCCMSSharedInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import yiyan.research.model.domain.Follow;
import yiyan.research.model.domain.openalex.*;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.WorkSummary4List;
import yiyan.research.model.entity.CoAuthor;
import yiyan.research.model.entity.CoIns;
import yiyan.research.model.entity.FollowedAuthor;
import yiyan.research.model.entity.InterestConceptByYear;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.model.request.*;
import yiyan.research.model.request.researcher.*;
import yiyan.research.model.response.*;
import yiyan.research.model.response.researcher.*;
import yiyan.research.service.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@RestController
@RequestMapping("/researcher")
@CrossOrigin(origins = "*")
public class ResearcherController {
    @Resource
    private FollowService followService;
    @Resource
    private AuthorService authorService;
    @Resource
    private RequestFormService requestFormService;
    @Resource
    private WorkService workService;
    @Resource
    private InstitutionService institutionService;
    @Resource
    private CacheService cacheService;
    @Resource
    private COSService cosService;
    //@Resource
    //private RocketMQService rocketMQService;

    @PostMapping("/getInfo")
    public ResponseEntity<GetInfoRsp> getInfo(@RequestBody GetInfoReq getInfoRequest, @RequestHeader(name = "token", required = false) String token) {
        String authorId = getInfoRequest.getAuthorId();
        int userId = -1;
       /* if(token!=null)
        {
            String userIdStr = rocketMQService.getUserId(token);
            if(userIdStr!=null) {
                userId = Integer.parseInt(userIdStr);
            }
        }//这个从token中来*/
        if(cacheService.get(getInfoRequest)!=null && userId == -1){
            return ResponseEntity.ok(cacheService.getObject(getInfoRequest, GetInfoRsp.class));
        }
        GetInfoRsp getInfoRsp = new GetInfoRsp();
        Authors author = authorService.getAuthorById(authorId);
        if(author == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        getInfoRsp.addAuthor(author);
        if(getInfoRsp.getDisplayInstitution()==null||Objects.equals(getInfoRsp.getDisplayInstitution(), "")){
            Institutions institution = institutionService.getInsById(author.getLastKnownInstitution());
            getInfoRsp.setDisplayInstitution(institution.getDisplayName());
        }
        boolean isFollow = followService.isFollow(String.valueOf(userId), authorId);
        getInfoRsp.setFollowing(isFollow);
        if(author.getUserId()!=null){
            getInfoRsp.setMine(author.getUserId().equals(String.valueOf(userId)));
            getInfoRsp.setClaimed(true);
        }
        if(userId == -1)cacheService.add(getInfoRequest, getInfoRsp);
        return ResponseEntity.ok(getInfoRsp);
    }
    @PostMapping("/updateInfo")
    public ResponseEntity<String> updateInfo(UpdateInfoReq updateInfoReq,@RequestHeader(name = "token", required = false) String token) {
        //System.out.println(updateInfoRequest.getId());
        boolean updateSuccess = false;

        String authorId = updateInfoReq.getId();
        //int  userId  = Integer.parseInt(rocketMQService.getUserId(token));//这个从token中来
        Authors author = authorService.getAuthorById(authorId);
        if(author == null){
            //
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("不存在此author");
        }
       /*if(author.getUserId()!=null&&!author.getUserId().equals(String.valueOf(userId))){
           //权限验证
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }*/
        String institution = updateInfoReq.getDisplayInstitution();
        String email = updateInfoReq.getEmail();
        String introduction = updateInfoReq.getIntroduction();
        String homepage = updateInfoReq.getHomepage();
        String googleScholar = updateInfoReq.getGoogleScholar();
        String education = updateInfoReq.getEducation();
        String experience = updateInfoReq.getExperience();
        MultipartFile avatar = updateInfoReq.getAvatar();
        if(institution != null && !institution.equals("")){
            author.setDisplayInstitution(institution);
        }
        if(email != null && !email.equals("")){
            author.setEmail(email);
        }
        if(introduction != null && !introduction.equals("")){
            author.setIntroduction(introduction);
        }
        if(homepage != null && !homepage.equals("")){
            author.setHomepage(homepage);
        }
        if(googleScholar != null && !googleScholar.equals("")){
            author.setGoogleScholar(googleScholar);
        }
        if(education != null && !education.equals("")){
            author.setEducation(education);
        }
        if(experience != null && !experience.equals("")){
            author.setExperience(experience);
        }

        if(avatar != null){
            String fileName = UUID.randomUUID()+".png";
            String path = "author-avatar/"+authorId+"/"+fileName;
            String avatarUrl = cosService.uploadAuthorAvatar(avatar,path);
            if(avatarUrl == null){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(null);
            }
            author.setAvatar(avatarUrl);
        }
        System.out.println(author.getId());
        if(authorService.updateAuthor(author) == 0){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(null);
        }
        return ResponseEntity.ok("update success");
    }
    @PostMapping("/getWorkStatistics")
    public ResponseEntity<GetWorkStatisticRsp> getWorkStatistics(@RequestBody GetWorkStatisticsReq req, HttpServletRequest request) {
        String authorId = req.getAuthorId();
        if(authorService.getAuthorById(authorId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        GetWorkStatisticRsp rsp = new GetWorkStatisticRsp();
        if(cacheService.get(req)!=null){
            rsp = cacheService.getObject(req, GetWorkStatisticRsp.class);
            //System.out.println("get from cache and expire time is "+cacheService.getExpire(getWorkListByEntry));
            return ResponseEntity.ok(rsp);
        }
        List<AuthorsCountsByYear> authorsCountsByYears = authorService.getAuthorsCountsByYearByAuthorId(authorId);
        rsp.setAuthorsCountsByYear(authorsCountsByYears);
        System.out.println("by year is ok");

        List<CoAuthorWorkCount> coAuthorWorkCounts = workService.getCoAuthorWorkCountList(authorId);
        rsp.setWorksCountByCoAuthor(coAuthorWorkCounts);
        System.out.println("by coAuthor is ok");

        List<CoInsWorkCount> coInsWorkCounts = workService.getCoInsWorkCountList(authorId);
        rsp.setWorksCountByCoIns(coInsWorkCounts);
        System.out.println("by coIns is ok");

        List<ConceptWorkCount> conceptWorkCounts = workService.getConceptWorkCount(authorId);
        rsp.setWorksCountByConcept(conceptWorkCounts);
        System.out.println("by concepts is ok");

        cacheService.add(req, rsp);
        return ResponseEntity.ok(rsp);
    }
    @PostMapping("/getWorkListByEntry")
    public ResponseEntity getWorkListByEntry(@RequestBody GetWorkListByEntryReq req){
        String authorId = req.getAuthorId();
        GetWorkListRsp rsp;
        List results = null;
        if(cacheService.get(req)!=null){
            rsp = cacheService.getObject(req, GetWorkListRsp.class);
            return ResponseEntity.ok(rsp);
        }
        rsp = new GetWorkListRsp();
        if(authorService.getAuthorById(authorId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        int prevNum = req.getPrevNum();
        int totalCount = 0;
        int year = req.getYear();

        if(year != 0){
            totalCount=workService.getWorkByAuthorByYear(authorId , year).size();
            results =workService.getWorkByAuthorByYear(authorId , year, prevNum);
        }else{
            int isByCited = req.getIsByCited();
            if(isByCited != 0){
                totalCount = workService.getWorkByAuthorIdOrderByCitedCount(authorId).size();
                results = workService.getWorkByAuthorIdOrderByCitedCount(authorId, prevNum);
            }else {
                String[] conceptIds = req.getConceptIds();
                if(conceptIds != null && conceptIds.length > 0){
                    System.out.println("concepts.length = " + conceptIds.length);
                    totalCount = workService.getWorkByAuthorIdAndConcepts(authorId, conceptIds).size();
                    results = workService.getWorkByAuthorIdAndConcepts(authorId, conceptIds, prevNum);
                } else {
                    String[] coAuthorIds = req.getCoAuthorIds();
                    if(coAuthorIds != null && coAuthorIds.length > 0){
                        totalCount = workService.getWorkByAuthorIdAndCoAuthors(authorId, coAuthorIds).size();
                        results = workService.getWorkByAuthorIdAndCoAuthors(authorId, coAuthorIds, prevNum);
                    }else {
                        String[] coInsIds = req.getCoInsIds();
                        if(coInsIds != null && coInsIds.length > 0){
                            totalCount =workService.getWorkByAuthorIdAndCoInss(authorId, coInsIds).size();
                            results = workService.getWorkByAuthorIdAndCoInss(authorId, coInsIds, prevNum);
                        }else {
                            //默认排序
                            totalCount = workService.getWorkByAuthorIdDefault(authorId).size();
                            results = workService.getWorkByAuthorIdDefault(authorId, prevNum);
                        }
                    }
                }
            }
        }
        workService.fillESWorkList(results);
        rsp.setResults(results);
        rsp.setTotalCount(totalCount);
        cacheService.add(req, rsp);
        return ResponseEntity.ok(rsp);
    }

    @PostMapping("/getRadarChart")
    public ResponseEntity<GetRadarChartRsp> getRadarChart(@RequestBody GetRadarChartReq req) {
        String authorId = req.getAuthorId();
        Authors author = authorService.getAuthorById(authorId);

        if(author == null){
            return null;
        }
        GetRadarChartRsp rsp;
        rsp = cacheService.getObject(req,GetRadarChartRsp.class);
        if(rsp!=null){
            return ResponseEntity.ok(rsp);
        }
        rsp = new GetRadarChartRsp();

        if(cacheService.get(req)!=null){
            rsp = cacheService.getObject(req,GetRadarChartRsp.class);
            return ResponseEntity.ok(rsp);
        }

        rsp.setPapers(author.getWorksCount());
        rsp.setCitation(author.getCitedByCount());
        rsp.setH_index(authorService.getAuthorsStatsByAuthorId(authorId).getHIndex());

        List<CoAuthorWorkCount> coAuthorWorkCounts = workService.getCoAuthorWorkCountList(authorId);
        List<CoInsWorkCount> coInsWorkCounts = workService.getCoInsWorkCountList(authorId);
        int sociability = coInsWorkCounts.size()+coAuthorWorkCounts.size();
        sociability = (int)  Math.round(Math.log(sociability));
        rsp.setSociability(sociability);

        List<ConceptWorkCount> conceptWorkCounts = workService.getConceptWorkCount(authorId);
        int diversity = conceptWorkCounts.size();
        diversity = (int) Math.round(Math.log(diversity));
        rsp.setDiversity(diversity);

        cacheService.add(req, rsp);
        return ResponseEntity.ok(rsp);
    }
    @PostMapping("/getCo")
    public ResponseEntity<GetCoRsp> getCo(@RequestBody GetCoReq req) {
        String authorId = req.getAuthorId();
        if(authorService.getAuthorById(authorId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        GetCoRsp rsp = new GetCoRsp();

        if(cacheService.get(req)!=null){
            rsp = cacheService.getObject(req,GetCoRsp.class);
            return ResponseEntity.ok(rsp);
        }

        List<CoAuthor> coAuthorList = authorService.getCoAuthorList(authorId);
        if (coAuthorList!=null) {
            rsp.setCoAuthorList(coAuthorList);
        }

        List<CoIns> coInsList = institutionService.getAuthorCoInsList(authorId);
        if (coInsList!=null) {
            rsp.setCoInsList(coInsList);
        }
        cacheService.add(req, rsp);
        return ResponseEntity.ok(rsp);
    }
    @PostMapping("/getOAWorks")
    public ResponseEntity<List<WorksAuthorships>> getOAWorks(@RequestBody GetOAWorksReq getOAWorksReq, HttpServletRequest request) {
        String authorId = getOAWorksReq.getAuthorId();
        if(authorService.getAuthorById(authorId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(
                workService.getOAWorksByAuthorId(authorId)
        );
    }
    @PostMapping("/follow")
    public ResponseEntity<StdStrRsp> follow(@RequestBody FollowReq followReq, @RequestHeader(name = "token", required = false) String token) {
        String id = followReq.getUserId();
        String authorId = followReq.getAuthorId();
        //int userIdToken = Integer.parseInt(rocketMQService.getUserId(token));
        /*if(!id.equals(String.valueOf(userIdToken))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }*/
        if(authorService.getAuthorById(authorId) == null)
            return new ResponseEntity<>(
                    new StdStrRsp("Author not found!"),
                    HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())
            );
        if(followService.isFollow(id, authorId))
            return new ResponseEntity<>(
                    new StdStrRsp("You are already following this author!"),
                    HttpStatusCode.valueOf(HttpStatus.CONFLICT.value())
            );
        if(followService.follow(id, authorId) == 0){
            return new ResponseEntity<>(new StdStrRsp("Follow fail!"), HttpStatusCode.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        }
        return ResponseEntity.ok(new StdStrRsp("Follow success!"));
    }
    @PostMapping("/unfollow")
    public ResponseEntity<StdStrRsp> unfollow(@RequestBody UnfollowReq unfollowReq, @RequestHeader(name = "token", required = false) String token) {
        String id = unfollowReq.getUserId();
        String authorId = unfollowReq.getAuthorId();
       // int userIdToken = Integer.parseInt(rocketMQService.getUserId(token));
       /* if(!id.equals(String.valueOf(userIdToken))){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }*/
        if(authorService.getAuthorById(authorId) == null)
            return new ResponseEntity<>(
                    new StdStrRsp("Author not found!"),
                    HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value())
            );
        if(!followService.isFollow(id, authorId))
            return new ResponseEntity<>(new StdStrRsp("You are not following this author!"), HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()));
        if(followService.unfollow(id, authorId) == 0){
            return new ResponseEntity<>(new StdStrRsp("Unfollow fail!"), HttpStatusCode.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()));
        }
        return ResponseEntity.ok(new StdStrRsp("Unfollow success!"));
    }

    @PostMapping("/getFollowList")
    public ResponseEntity<GetFollowListRsp> getFollowList(@RequestBody GetFollowListReq getFollowListReq, @RequestHeader(name = "token", required = false) String token) {

        String userId = getFollowListReq.getUserId();
        if(token!=null){
           // work.setLike(rocketMQService.getWorkIsLikeByUser(token, work.getId()));
           // int tokenUserId  = Integer.parseInt(rocketMQService.getUserId(token));
            /*if(!String.valueOf(tokenUserId).equals(userId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }*/
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        GetFollowListRsp rsp = new GetFollowListRsp();
        List<Follow> followList = followService.getFollowList(userId);
        for(Follow follow:followList){
            Authors authorsFollowed=authorService.getAuthorById(follow.getAuthorId());
            FollowedAuthor followedAuthor = new FollowedAuthor();
            followedAuthor.setAuthorName(authorsFollowed.getDisplayName());
            followedAuthor.setAuthorId(authorsFollowed.getId());
            followedAuthor.setAvatarUrl(authorsFollowed.getAvatar());
            followedAuthor.setEmail(authorsFollowed.getEmail());
            if(authorsFollowed.getDisplayInstitution()!=null){
                followedAuthor.setInsName(authorsFollowed.getDisplayInstitution());
            }else{
                Institutions institutions = institutionService.getInsById(authorsFollowed.getLastKnownInstitution());
                followedAuthor.setInsName(institutions.getDisplayName());
            }
            followedAuthor.setFollowing(true);
            rsp.getFollowList().add(followedAuthor);
        }
        rsp.setFollowingNum(followList.size());

        return ResponseEntity.ok(rsp);
    }

    @PostMapping("/getFanList")
    public ResponseEntity<List> getFanList(@RequestBody GetFanListReq getFanListReq, HttpServletRequest request) {

        return null;
    }

    @PostMapping("/verifyAuthorByEmail")
    public ResponseEntity<StdStrRsp> verifyAuthorByEmail(@RequestBody VerifyAuthorByEmailReq verifyAuthorByEmailReq, @RequestHeader(name = "token", required = false) String token) {
        String userId = verifyAuthorByEmailReq.getUserId();
        if(token!=null){
            // work.setLike(rocketMQService.getWorkIsLikeByUser(token, work.getId()));
            //int tokenUserId  = Integer.parseInt(rocketMQService.getUserId(token));
            /*if(!String.valueOf(tokenUserId).equals(userId)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }*/
        }else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        String authorId = verifyAuthorByEmailReq.getAuthorId();
        Authors author = authorService.getAuthorById(authorId);
        if(author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StdStrRsp("Author not found!"));
        }
        if(author.getUserId() != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new StdStrRsp("This author has already been claimed!"));
        }
        String email = author.getEmail();
        if(email == null || email.equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StdStrRsp("This author has no email!"));
        }
       /* if(rocketMQService.sendAuthorApplication(token,email)){
            return ResponseEntity.ok(new StdStrRsp("Application submitted!"));
        }else{*/
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new StdStrRsp("Service unavailable!"));
       // }
        /*if(requestFormService.addApplyForm(userId,authorId,content,attachmentUrl) == 0){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new StdStrRsp("Service unavailable!"));
        }*/
    }
    @PostMapping("/verifyCode")
    public ResponseEntity<StdStrRsp> verifyCode(@RequestBody VerifyCodeReq req, @RequestHeader(name = "token", required = false) String token) {
        String code = req.getCode();
        String authorId = req.getAuthorId();
      /*  if(token!=null) {
            // work.setLike(rocketMQService.getWorkIsLikeByUser(token, work.getId()));
           // int tokenUserId = Integer.parseInt(rocketMQService.getUserId(token));
            if (tokenUserId == -1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }*/
        Authors author = authorService.getAuthorById(authorId);
        if(author == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StdStrRsp("Author not found!"));
        String email = author.getEmail();
        if(email == null || email.equals("")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StdStrRsp("This author has no email!"));
        }
        /*if(rocketMQService.authCode(token,email,code)) {
            return ResponseEntity.ok(new StdStrRsp("Application submitted!"));
        }else{*/
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new StdStrRsp("Service unavailable!"));
        //}
    }
    @PostMapping("/claimWork")
    public ResponseEntity<StdStrRsp> claimWork(@RequestBody ClaimWorkReq claimWorkReq, @RequestHeader(name = "token", required = false) String token) {
        String authorId = claimWorkReq.getAuthorId();
        String workId = claimWorkReq.getWorkId();
        Authors author = authorService.getAuthorById(authorId);
        String userId = author.getUserId();
//        if(token!=null){
//            // work.setLike(rocketMQService.getWorkIsLikeByUser(token, work.getId()));
//            //int tokenUserId  = Integer.parseInt(rocketMQService.getUserId(token));
////            if(!String.valueOf(tokenUserId).equals(userId)){
////                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
////            }
//        }else {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//        }
        if(author == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StdStrRsp("Author not found!"));
        List<WorksAuthorships> worksAuthorships = workService.getWorksAuthorshipsByWorkId(workId);
        for(WorksAuthorships worksAuthorship: worksAuthorships){
            if(worksAuthorship.getAuthorId().equals(authorId)){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new StdStrRsp("This work has already been claimed by this author!"));
            }
        }
        WorksAuthorships claim = new WorksAuthorships();
        claim.setAuthorId(authorId);
        claim.setWorkId(workId);
        claim.setInstitutionId(author.getLastKnownInstitution());
        claim.setAuthorPosition("belong to this author");
        if(workService.addWorkAuthorship(claim) == 0){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new StdStrRsp("Service unavailable!"));
        }
        authorService.addAuthorWorkCount(authorId);
        return ResponseEntity.ok(new StdStrRsp("Claim success!"));
    }
    @PostMapping("/deleteWork")
    public String deleteWork() {

        return "deleteWork";
    }
    @PostMapping("/appealPortalIssues")
    public String appealPortalIssues() {

        return "appealPortalIssues";
    }
    @PostMapping("getInterests")
    public ResponseEntity<GetInterestsRsp> getInterests(@RequestBody GetInterestsReq req) {
        String authorId = req.getAuthorId();
        if(authorService.getAuthorById(authorId) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        GetInterestsRsp rsp = new GetInterestsRsp();
        List<InterestConceptByYear> interestConceptByYears = workService.getInterestConceptByYear(authorId);
        rsp.setConceptsByYear(interestConceptByYears);
        return ResponseEntity.ok(rsp);
    }
}