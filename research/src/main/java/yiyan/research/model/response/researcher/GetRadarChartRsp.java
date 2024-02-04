package yiyan.research.model.response.researcher;

import lombok.Data;

@Data
public class GetRadarChartRsp {
    //radar chart
    private int papers;
    private int citation;
    private int h_index;
    private int diversity;
    private int sociability;
}
