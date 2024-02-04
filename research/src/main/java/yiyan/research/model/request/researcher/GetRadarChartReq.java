package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class GetRadarChartReq {
    private String authorId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetRadarChartReq)) return false;
        GetRadarChartReq that = (GetRadarChartReq) o;
        return getAuthorId().equals(that.getAuthorId());
    }
}
