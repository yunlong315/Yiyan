package yiyan.research.model.response.ins;

import lombok.Data;

@Data
public class GetWorkListReq {
    private String institutionId;
    private int year;
    private int isByCited;
    private int prevNum;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetWorkListReq)) return false;
        GetWorkListReq that = (GetWorkListReq) o;
        return getYear() == that.getYear() &&
                getIsByCited() == that.getIsByCited() &&
                getPrevNum() == that.getPrevNum() &&
                getInstitutionId().equals(that.getInstitutionId());
    }
}
