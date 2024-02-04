package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.entity.InterestConceptByYear;

import java.util.List;

@Data
public class GetInterestsRsp {
    List<InterestConceptByYear> conceptsByYear;
}
