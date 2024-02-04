package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.entity.FollowedAuthor;

import java.util.List;

@Data
public class GetFollowListRsp {
    List<FollowedAuthor> followList;
    int followingNum;
}
