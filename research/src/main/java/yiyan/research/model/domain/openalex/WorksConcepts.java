package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksConcepts {
    private String workId;
    private String conceptId;
    private Float score;
}