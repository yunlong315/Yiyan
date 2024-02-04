package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class InstitutionsCountsByYear {
    private String institutionId;
    private Integer year;
    private Integer worksCount;
    private Integer citedByCount;
}