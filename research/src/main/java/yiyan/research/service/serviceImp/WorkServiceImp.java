package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.AuthorMapper;
import yiyan.research.mapper.ConceptMapper;
import yiyan.research.mapper.InstitutionMapper;
import yiyan.research.mapper.WorkMapper;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.Works;
import yiyan.research.model.domain.openalex.WorksAuthorships;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.InterestConceptByYear;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.service.WorkService;

import java.util.List;

@Service
public class WorkServiceImp implements WorkService {
    @Resource
    private WorkMapper workMapper;
    @Resource
    private AuthorMapper authorMapper;
    @Resource
    private InstitutionMapper insMapper;
    @Resource
    private ConceptMapper conceptMapper;

    @Override
    public List<WorksAuthorships> getWorksAuthorshipsByWorkId(String id) {
        return workMapper.getWorksAuthorshipsByWorkId(id);
    }

    @Override
    public List<WorksAuthorships> getOAWorksByAuthorId(String id) {
        return workMapper.getOAWorksByAuthorId(id);
    }

    @Override
    public List<CoAuthorWorkCount> getCoAuthorWorkCountList(String authorId) {
        List<CoAuthorWorkCount> list = workMapper.getCoAuthorWorkCountList(authorId);
        return list;
    }

    @Override
    public List<CoInsWorkCount> getCoInsWorkCountList(String authorId) {
        List<CoInsWorkCount> list = workMapper.getCoInsWorkCountList(authorId);
        return list;
    }

    @Override
    public List<ConceptWorkCount> getConceptWorkCount(String authorId) {
        List<ConceptWorkCount> list = workMapper.getConceptWorkCount(authorId);
        return list;
    }

    @Override
    public void fillESWorkList(List<ESWork> esWorks){
        for (ESWork item : esWorks) {
            Works works = new Works();
            works.setAbstractInvertedIndex(item.getAbstractText());
            item.setAbstractText(works.generateAbstract());
            List<ESWork.Author> authorInWorks = workMapper.getAuthorInWork(item.getId());
            item.setAuthors(authorInWorks);
            List<ESWork.Concept> concepts = workMapper.getESWorksConceptsById(item.getId());
            item.setConcepts(concepts);
            item.setSource(workMapper.getESWorksSourceById(item.getId()));
            WorksInfo worksInfo = workMapper.getWorksInfo(item.getId());
            if(worksInfo!=null)
                item.setViewCount(worksInfo.getViewCount());
            else item.setViewCount(0);
           /* List<WorksConcepts> wcs = workMapper.getWorksConceptsById(item.getId());
            for (WorksConcepts wc : wcs) {
                ESWork.Concept concept = new ESWork.Concept();
                concept.setId(wc.getConceptId());
                concept.setDisplayName(conceptMapper.getConceptById(wc.getConceptId()).getDisplayName());
                item.getConcepts().add(concept);
            }*/
        }
    }
    @Override
    public List<ESWork> getWorkByAuthorByYear(String authorId, int year) {
        return workMapper.getWorksByAuthorIdByYear(authorId, year);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdOrderByCitedCount(String authorId) {
        return workMapper.getWorksByAuthorIdOrderByCitedCount(authorId);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndConcepts(String authorId, String[] conceptId) {
        return workMapper.getWorksByAuthorIdAndConcepts(authorId, conceptId);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndCoAuthors(String authorId, String[] coAuthorIds) {
        return workMapper.getWorksByAuthorIdAndCoAuthors(authorId, coAuthorIds);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndCoInss(String authorId, String[] coInsIds) {

        return workMapper.getWorksByAuthorIdAndCoInss(authorId, coInsIds);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdDefault(String authorId) {

        return workMapper.getWorksByAuthorIdDefault(authorId);
    }

    @Override
    public List<ESWork> getWorkByAuthorByYear(String authorId, int year, int prevNum) {
        return workMapper.getWorksByAuthorIdByYearByPervNum(authorId, year, prevNum);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdOrderByCitedCount(String authorId, int prevNum) {
        return workMapper.getWorksByAuthorIdOrderByCitedCountByPervNum(authorId, prevNum);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndConcepts(String authorId, String[] conceptId, int prevNum) {
        return workMapper.getWorksByAuthorIdAndConceptsByPervNum(authorId, conceptId, prevNum);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndCoAuthors(String authorId, String[] coAuthorIds, int prevNum) {
        return workMapper.getWorksByAuthorIdAndCoAuthorsByPervNum(authorId, coAuthorIds, prevNum);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdAndCoInss(String authorId, String[] coInsIds, int prevNum) {

        return workMapper.getWorksByAuthorIdAndCoInssByPervNum(authorId, coInsIds, prevNum);
    }

    @Override
    public List<ESWork> getWorkByAuthorIdDefault(String authorId, int prevNum) {

        return workMapper.getWorksByAuthorIdDefaultByPervNum(authorId, prevNum);
    }


    @Override
    public List<ESWork> getWorkByInsIdByYear(String insId, int year) {
        List<ESWork> worksSummarys4List = workMapper.getWorksByInsIdByYear(insId, year);
        return worksSummarys4List;
    }
    @Override
    public List<ESWork> getWorkByInsIdOrderByCitedCount(String insId) {

        return workMapper.getWorksByInsIdOrderByCitedCount(insId);
    }
    @Override
    public List<ESWork> getWorkByInsIdDefault(String insId) {
        return workMapper.getWorksByInsIdDefault(insId);
    }


    @Override
    public List<ESWork> getWorkByInsIdByYear(String insId, int year, int prevNum) {
        List<ESWork> worksSummarys4List = workMapper.getWorksByInsIdByYearByPrevNum(insId, year, prevNum);
        return worksSummarys4List;
    }
    @Override
    public List<ESWork> getWorkByInsIdOrderByCitedCount(String insId, int prevNum) {

        return workMapper.getWorksByInsIdOrderByCitedCountByPrevNum(insId, prevNum);
    }
    @Override
    public List<ESWork> getWorkByInsIdDefault(String insId, int prevNum) {
        return workMapper.getWorksByInsIdDefaultByPrevNum(insId,prevNum);
    }

    @Override
    public int addWorkAuthorship(WorksAuthorships wa) {
        return workMapper.addWorkAuthorship(wa);
    }

    @Override
    public int deleteWorkAuthorship(String authorId, String workId) {
        return workMapper.deleteWorkAuthorship(authorId, workId);
    }

    @Override
    public List<ESWork> getHotWorks(List<String> worksId) {
        return null;
    }

    @Override
    public void addWorkViewCount(String workId) {
        WorksInfo worksInfo = workMapper.getWorksInfo(workId);
        if(worksInfo == null){
            workMapper.initWorksInfo(workId);
        }
        workMapper.addWorkViewCount(workId);
    }

    @Override
    public WorksInfo getWorksInfo(String workId) {
        WorksInfo worksInfo = workMapper.getWorksInfo(workId);
        if(worksInfo == null){
            worksInfo = new WorksInfo(workId);
            //workMapper.initWorksInfo(workId);
        }
        return worksInfo;
    }

    @Override
    public List<InterestConceptByYear> getInterestConceptByYear(String authorId) {
        return workMapper.getInterestConceptByYear(authorId);
    }

}
