package yiyan.research.model.entity.AuthorWorkStatistic;

import lombok.Data;
import yiyan.research.model.entity.AuthorInWork;

import java.util.List;

@Data
public class WorkSummary4List {
    private String workId;
    private String title;
    private int publicationYear;
    private int citedByCount;
    private List<AuthorInWork> authors;
}
