package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsGeo;

import java.util.List;

@Data
public class GetInsInfoRsp {
    //basic info
    private String institutionId;
    private String institutionName;
    private String avatarUrl;
    private int paperCount;
    private int citation;
    private int h_index;
    private double impactFactor;
    private int sociability;
}
