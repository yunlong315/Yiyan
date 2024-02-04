package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class InstitutionsAssociatedInstitutions {
    private String institutionId;
    private String associatedInstitutionId;
    private String relationship;
}