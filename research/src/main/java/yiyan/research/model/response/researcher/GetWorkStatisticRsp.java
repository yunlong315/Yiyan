package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.YearWorkCount;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetWorkStatisticRsp {
    private List<YearWorkCount> worksCountByYear;
    private List<ConceptWorkCount> worksCountByConcept;
    private List<CoAuthorWorkCount> worksCountByCoAuthor;
    private List<CoInsWorkCount> worksCountByCoIns;


    public void setAuthorsCountsByYear(List<AuthorsCountsByYear> authorsCountsByYears) {
        this.worksCountByYear = new ArrayList<>();
        for (AuthorsCountsByYear authorsCountsByYear : authorsCountsByYears) {
            YearWorkCount yearWorkCount = new YearWorkCount();
            yearWorkCount.setYear(authorsCountsByYear.getYear());
            yearWorkCount.setCount(authorsCountsByYear.getWorksCount());
            worksCountByYear.add(yearWorkCount);
        }
    }


}