package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.entity.CoAuthor;
import yiyan.research.model.entity.CoIns;

import java.util.List;

@Data
public class GetCoRsp {
    private List<CoAuthor> coAuthorList;
    private List<CoIns> coInsList;
}
