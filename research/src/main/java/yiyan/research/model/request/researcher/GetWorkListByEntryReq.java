package yiyan.research.model.request.researcher;

import lombok.Data;

import java.util.Arrays;
import java.util.Objects;

@Data
public class GetWorkListByEntryReq {
    private String authorId;
    private int year;
    private int isByCited;
    private String[] conceptIds;
    private String[] coAuthorIds;
    private String[] coInsIds;
    private int prevNum;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetWorkListByEntryReq that)) return false;
        if(!Objects.equals(that.authorId, authorId)) return false;
        if(!Objects.equals(that.year, year)) return false;
        if(!Objects.equals(that.isByCited, isByCited)) return false;
        if(!Arrays.equals(that.conceptIds, conceptIds)) return false;
        if(!Arrays.equals(that.coAuthorIds, coAuthorIds)) return false;
        if(!Arrays.equals(that.coInsIds, coInsIds)) return false;
        return true;
    }
}
