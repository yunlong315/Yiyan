package yiyan.research.model.domain.openalex;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Sources {
    private String id;
    private String issnL;
    private String issn;
    private String displayName;
    private String publisher;
    private Integer worksCount;
    private Integer citedByCount;
    private Boolean isOa;
    private Boolean isInDoaj;
    private String homepageUrl;
    private String worksApiUrl;
    private Timestamp updatedDate;
}
