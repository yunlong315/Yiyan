package yiyan.research.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import yiyan.research.model.domain.Follow;
import yiyan.research.model.domain.openalex.Authors;

import java.util.Date;
import java.util.List;

@Mapper
public interface FollowMapper {

    @Insert("insert into follow (user_id, author_id, create_time) values (#{userId}, #{authorId}, #{createTime})")
    public int follow(String userId, String authorId, Date createTime);

    @Select("select count(*) from follow where user_id=#{userId} and author_id=#{authorId}")
    int isFollow(String userId, String authorId);

    @Delete("delete from follow where user_id=#{userId} and author_id=#{authorId}")
    int unfollow(String userId, String authorId);

    @Select("select * from follow where follow.user_id = #{userId}")
    List<Follow> getFollowList(String userId);
}
