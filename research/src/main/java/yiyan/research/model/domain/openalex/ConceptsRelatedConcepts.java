package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class ConceptsRelatedConcepts {
    private String conceptId;
    private String relatedConceptId;
    private Float score;
}