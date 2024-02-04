package yiyan.research.model.request.researcher;

import lombok.Data;

@Data
public class GetInfoReq {
    private String authorId;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof GetInfoReq that)) return false;
        return authorId.equals(that.authorId);
    }

}
