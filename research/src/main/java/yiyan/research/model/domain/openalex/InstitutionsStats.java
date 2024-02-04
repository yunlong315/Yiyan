package yiyan.research.model.domain.openalex;

import lombok.Data;
import org.apache.ibatis.annotations.Param;

@Data
public class InstitutionsStats {
    private String institutionId;
    private int hIndex;
    private int i10Index;
    private double yr_mean_citedness;
}
