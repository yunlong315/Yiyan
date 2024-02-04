package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class ConceptsCountsByYear {
    private String conceptId;
    private Integer year;
    private Integer worksCount;
    private Integer citedByCount;
}