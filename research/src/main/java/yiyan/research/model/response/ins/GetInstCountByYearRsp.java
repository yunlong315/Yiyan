package yiyan.research.model.response.ins;

import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;

import java.util.ArrayList;
import java.util.List;

/**
 * Request
 */
@lombok.Data
public class GetInstCountByYearRsp {
    private List<InstitutionsCountsByYear> timeLine;

    public GetInstCountByYearRsp(List<InstitutionsCountsByYear> list){
        timeLine = list;
    }
}
