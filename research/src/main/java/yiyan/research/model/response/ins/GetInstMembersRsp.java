package yiyan.research.model.response.ins;

import lombok.Data;
import yiyan.research.model.entity.AuthorInInstitution;

import java.util.List;

@Data
public class GetInstMembersRsp {
    List<AuthorInInstitution> authors;
    public GetInstMembersRsp(List<AuthorInInstitution> list){
        authors = list;
    }
}
