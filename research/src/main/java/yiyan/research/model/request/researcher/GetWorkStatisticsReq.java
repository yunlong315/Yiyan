package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class GetWorkStatisticsReq {
    private String authorId;
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GetWorkStatisticsReq getWorkStatisticsReq)) {
            return false;
        }
        return authorId.equals(getWorkStatisticsReq.authorId);
    }
}
