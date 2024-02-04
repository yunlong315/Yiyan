package yiyan.research.model.request.search;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class GetRelatedWorksReq {
    List<String> relatedWorks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GetRelatedWorksReq that = (GetRelatedWorksReq) o;
        return Objects.equals(relatedWorks, that.relatedWorks);
    }

}
