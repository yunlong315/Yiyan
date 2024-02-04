package yiyan.research.model.response.search;

import kotlin.UByte;
import lombok.Data;
import yiyan.research.model.esEntity.ESWork;

import java.util.List;

@Data
public class HotPaperRsp {
    private List<ESWork> results;

    public HotPaperRsp(List<ESWork> list){
        results = list;
    }

    public HotPaperRsp(){}
}
