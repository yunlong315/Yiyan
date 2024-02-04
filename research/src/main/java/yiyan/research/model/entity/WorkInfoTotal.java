package yiyan.research.model.entity;

import lombok.Data;
import yiyan.research.model.domain.openalex.Concepts;

import java.util.List;

@Data
public class WorkInfoTotal {
    private String id;
    private String doi;
    private String title;
    private String displayName;
    private Integer publicationYear;
    private String publicationDate;
    private String type;
    private Integer citedByCount;
    private Boolean isRetracted;
    private Boolean isParatext;
    private String citedByApiUrl;
    private String abstractInvertedIndex;

    private List<AuthorInWork> authorInWork;
    private List<Concepts> concepts;

}
