package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksReferencedWorks {
    private String workId;
    private String referencedWorkId;
}