package yiyan.research.service;

import org.springframework.stereotype.Service;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.WorksAuthorships;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.WorkSummary4List;
import yiyan.research.model.entity.InterestConceptByYear;
import yiyan.research.model.esEntity.ESWork;

import java.util.List;

@Service
public interface WorkService {
    List<WorksAuthorships> getWorksAuthorshipsByWorkId(String id);

    List<WorksAuthorships> getOAWorksByAuthorId(String id);
    List<ConceptWorkCount> getConceptWorkCount(String authorId);
    List<CoAuthorWorkCount> getCoAuthorWorkCountList(String authorId);
    List<CoInsWorkCount> getCoInsWorkCountList(String authorId);

    void fillESWorkList(List<ESWork> esWorks);

    List<ESWork> getWorkByAuthorByYear(String authorId, int year);
    List<ESWork> getWorkByAuthorIdOrderByCitedCount(String authorId);

    List<ESWork> getWorkByAuthorIdAndConcepts(String authorId, String[] conceptId);

    List<ESWork> getWorkByAuthorIdAndCoAuthors(String authorId, String[] coAuthorIds);

    List<ESWork> getWorkByAuthorIdAndCoInss(String authorId, String[] coInsId);

    List<ESWork> getWorkByAuthorIdDefault(String authorId);

    List<ESWork> getWorkByAuthorByYear(String authorId, int year, int prevNum);
    List<ESWork> getWorkByAuthorIdOrderByCitedCount(String authorId, int prevNum);

    List<ESWork> getWorkByAuthorIdAndConcepts(String authorId, String[] conceptId, int prevNum);

    List<ESWork> getWorkByAuthorIdAndCoAuthors(String authorId, String[] coAuthorIds, int prevNum);

    List<ESWork> getWorkByAuthorIdAndCoInss(String authorId, String[] coInsId, int prevNum);

    List<ESWork> getWorkByAuthorIdDefault(String authorId, int prevNum);

    List<ESWork> getWorkByInsIdByYear(String insId, int year);
    List<ESWork> getWorkByInsIdOrderByCitedCount(String insId);
    List<ESWork> getWorkByInsIdDefault(String insId);
    List<ESWork> getWorkByInsIdByYear(String insId, int year, int prevNum);
    List<ESWork> getWorkByInsIdOrderByCitedCount(String insId, int prevNum);
    List<ESWork> getWorkByInsIdDefault(String insId, int prevNum);


    WorksInfo getWorksInfo(String workId);

    int addWorkAuthorship(WorksAuthorships wa);

    int deleteWorkAuthorship(String authorId, String workId);

    List<ESWork> getHotWorks(List<String> worksId);

    void addWorkViewCount(String workId);

    List<InterestConceptByYear> getInterestConceptByYear(String authorId);
}
