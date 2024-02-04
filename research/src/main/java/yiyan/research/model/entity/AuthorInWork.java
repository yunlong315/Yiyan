package yiyan.research.model.entity;

import lombok.Data;
import yiyan.research.model.domain.openalex.WorksAuthorships;

@Data
public class AuthorInWork {
    private String workId;
    private String authorId;
    private String authorName;
    private String authorPosition;
    private String rawAffiliationString;
}
