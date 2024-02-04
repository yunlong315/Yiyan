package yiyan.research.model.response.ins;

import lombok.Data;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.YearWorkCount;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetInsStatisticRsp {
    private List<YearWorkCount> worksCountByYear;

    public void addInstitutionsCountsByYear(List<InstitutionsCountsByYear> institutionsCountsByYears) {
        this.worksCountByYear = new ArrayList<>();
        for (InstitutionsCountsByYear authorsCountsByYear : institutionsCountsByYears) {
            YearWorkCount yearWorkCount = new YearWorkCount();
            yearWorkCount.setYear(authorsCountsByYear.getYear());
            yearWorkCount.setCount(authorsCountsByYear.getWorksCount());
            worksCountByYear.add(yearWorkCount);
        }
    }

}