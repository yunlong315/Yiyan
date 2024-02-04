package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksAuthorships {
    private String workId;
    private String authorPosition;
    private String authorId;
    private String institutionId;
    private String rawAffiliationString;
}