package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksOpenAccess {
    private String workId;
    private String isOa;
    private String oaStatus;
    private String oaUrl;
}