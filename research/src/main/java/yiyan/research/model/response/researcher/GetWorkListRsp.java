package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.esEntity.ESWork;

import java.util.List;

@Data
public class GetWorkListRsp {
    private List<ESWork> results;
    private int totalCount;
}
