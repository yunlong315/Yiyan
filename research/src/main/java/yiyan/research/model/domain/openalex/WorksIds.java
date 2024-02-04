package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksIds {
    private String workId;
    private String openalex;
    private String doi;
    private String mag;
    private String pmid;
    private String pmcid;
}