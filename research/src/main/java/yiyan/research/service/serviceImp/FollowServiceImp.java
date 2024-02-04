package yiyan.research.service.serviceImp;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import yiyan.research.mapper.FollowMapper;
import yiyan.research.model.domain.Follow;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.service.FollowService;

import java.util.Date;
import java.util.List;

@Service
public class FollowServiceImp implements FollowService {

    @Resource
    private FollowMapper followMapper;

    @Override
    public int follow(String userId, String authorId) {
        return followMapper.follow(userId, authorId, new Date());
    }
    @Override
    public boolean isFollow(String userId, String authorId) {
        return followMapper.isFollow(userId, authorId) > 0;
    }
    @Override
    public int unfollow(String userId, String authorId) {
        return followMapper.unfollow(userId, authorId);
    }
    @Override
    public List<Follow> getFollowList(String userId) {
        return followMapper.getFollowList(userId);
    }
}
