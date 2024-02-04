package yiyan.research.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsStats;
import yiyan.research.model.entity.AuthorInInstitution;
import yiyan.research.model.entity.CoIns;
import yiyan.research.model.request.ins.InstIdReq;
import yiyan.research.model.request.researcher.GetInsInfoReq;
import yiyan.research.model.response.ins.GetInsStatisticRsp;
import yiyan.research.model.response.ins.GetInstCountByYearRsp;
import yiyan.research.model.response.ins.GetInstMembersRsp;
import yiyan.research.model.response.ins.GetWorkListReq;
import yiyan.research.model.response.researcher.GetInsInfoRsp;
import yiyan.research.model.response.researcher.GetWorkListRsp;
import yiyan.research.service.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/institution")
@CrossOrigin(origins = "*")
public class InsController {
    @Resource
    private InstitutionService institutionService;
    @Resource
    private SearchService searchService;
    @Resource
    private WorkService workService;
    @Resource
    private RocketMQService rocketMQService;
    @Resource
    private CacheService cacheService;


    @PostMapping("/getInfo")
    public ResponseEntity<GetInsInfoRsp> getInstitutionInfo(@RequestBody GetInsInfoReq getInsInfoReq, HttpServletRequest request) {
        GetInsInfoRsp rsp = new GetInsInfoRsp();

        if(cacheService.get(getInsInfoReq)!=null){
            rsp = cacheService.getObject(getInsInfoReq,GetInsInfoRsp.class);
            return ResponseEntity.ok(rsp);
        }

        String institutionId = getInsInfoReq.getInstitutionId();
        Institutions institution = institutionService.getInsById(institutionId);
        if (institution == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        rsp.setInstitutionId(institutionId);
        rsp.setInstitutionName(institution.getDisplayName());
        rsp.setAvatarUrl(institution.getImageUrl());
        rsp.setPaperCount(institution.getWorksCount());
        rsp.setCitation(institution.getCitedByCount());

        InstitutionsStats institutionsStats = institutionService.getInsStatsById(institutionId);
        rsp.setH_index(institutionsStats.getHIndex());
        rsp.setImpactFactor(institutionsStats.getYr_mean_citedness());

        int sociability = institutionService.getCoInsCountById(institutionId);
        sociability = (int) Math.round(Math.log(sociability));
        rsp.setSociability(sociability);
        return ResponseEntity.ok(rsp);
    }

    @PostMapping("/getWorkList")
    public ResponseEntity getWorkList(@RequestBody GetWorkListReq getWorkListReq, HttpServletRequest request) {
        String insId = getWorkListReq.getInstitutionId();
        int year = getWorkListReq.getYear();
        int isByCited = getWorkListReq.getIsByCited();
        int prevNum = getWorkListReq.getPrevNum();
        Institutions ins = institutionService.getInsById(insId);
        GetWorkListRsp rsp;
        rsp = cacheService.getObject(getWorkListReq,GetWorkListRsp.class);
        if(rsp!=null){
            return ResponseEntity.ok(rsp);
        }
        rsp = new GetWorkListRsp();
        int totalCount = 0;
        List results = null;
        if (ins == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        if (year != 0) {
            totalCount = workService.getWorkByInsIdByYear(insId, year).size();
            results = workService.getWorkByInsIdByYear(insId, year, prevNum);
        } else if (isByCited != 0) {
            totalCount =  workService.getWorkByInsIdOrderByCitedCount(insId).size();
            results = workService.getWorkByInsIdOrderByCitedCount(insId, prevNum);
        } else {
            totalCount = workService.getWorkByInsIdDefault(insId).size();
            results = workService.getWorkByInsIdDefault(insId, prevNum);
        }
        workService.fillESWorkList(results);
        rsp.setResults(results);
        rsp.setTotalCount(totalCount);
        cacheService.add(getWorkListReq,rsp);
        return ResponseEntity.ok(rsp);
    }

    @PostMapping("/getTimeline")
    public ResponseEntity<GetInstCountByYearRsp> getInstitutionCountByYear(@RequestBody InstIdReq request, HttpServletRequest header) {
        String instId = request.getInstitutionId();
        return ResponseEntity.ok(new GetInstCountByYearRsp(institutionService.getInsCountByYear(instId)));
    }

    @PostMapping("/getMembers")
    public ResponseEntity<GetInstMembersRsp> getInstMembers(@RequestBody InstIdReq request, HttpServletRequest header) {
        String instId = request.getInstitutionId();
        List<AuthorInInstitution> authors = institutionService.getInstMembers(instId);
        authors.sort(Comparator.comparing(AuthorInInstitution::getHIndex).reversed());
        authors = authors.subList(0, request.getMaxCount());
        return ResponseEntity.ok(new GetInstMembersRsp(authors));
    }


    @PostMapping("/getCoIns")
    public ResponseEntity<List<CoIns>> getCoIns(@RequestBody InstIdReq instIdReq, HttpServletRequest header) {
        String insId = instIdReq.getInstitutionId();
        Institutions institutions = institutionService.getInsById(insId);
        if (institutions == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<CoIns> coInsList = institutionService.getInsCoInsList(insId);
        return ResponseEntity.ok(coInsList);
    }

    @PostMapping("getWorkStatistics")
    public ResponseEntity<GetInsStatisticRsp> getStatistics(@RequestBody InstIdReq instIdReq, HttpServletRequest header) {
        String insId = instIdReq.getInstitutionId();
        List<InstitutionsCountsByYear> institutionsCountsByYears = institutionService.getInsCountByYear(insId);
        if(institutionsCountsByYears==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        GetInsStatisticRsp rsp = new GetInsStatisticRsp();
        rsp.addInstitutionsCountsByYear(institutionsCountsByYears);
        return ResponseEntity.ok(rsp);
    }
}