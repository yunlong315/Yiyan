package yiyan.research.mapper;

import org.apache.ibatis.annotations.*;
import yiyan.research.model.domain.Comment;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.*;

@Mapper
public interface WorkDetailMapper {
    @Select("SELECT * FROM works WHERE id=#{id}")
    Works getInfo(String id);
    //创建评论/回复评论
    //删除评论
    //获取所有评论
    @Insert("INSERT INTO Comment (userId, created_at, content, parent_id, work_id) " +
            "VALUES (#{userId}, #{created_at}, #{content}, #{parent_id}, #{work_id})")
    void insertComment(Comment c);

    // Delete Comment
    @Delete("DELETE FROM Comment WHERE id = #{id}")
    void deleteComment(int id);

    @Select("SELECT * FROM Comment WHERE id = #{id}")
    Comment getComment(int id);
    // Get All Comments for a Work
    @Select("SELECT * FROM Comment WHERE work_id = #{work_id} AND parent_id = 0")
    Comment[] getAllComment(String work_id);

    // Get All Replies for a Comment
    @Select("SELECT * FROM Comment WHERE work_id = #{work_id} AND parent_id = #{parent_id}")
    Comment[] getAllReply(String work_id, int parent_id);


    @Select("SELECT * FROM works_info WHERE work_id = #{work_id}")
    WorksInfo getWorkInfo(String work_id);

    @Insert("insert into works_info (work_id, is_comment_allowed) values (#{work_id}, true)")
    void insertWorks_info(String work_id);

    @Update("update works_info set is_comment_allowed =#{is_comment_allowed} where work_id = #{work_id}")
    void  updateCommentPermission(String work_id,boolean is_comment_allowed);

    @Select("SELECT * FROM works_authorships WHERE work_id = #{work_id} and author_id = #{authorId}")
    WorksAuthorships getAuthorships(String work_id,String authorId);

    @Update("UPDATE User SET attachmentUrl=#{attachmentUrl}  WHERE work_id = #{work_id}")
    void updateAttachment(String url,String work_id);

    @Select("SELECT * FROM works_authorships WHERE work_id = #{work_id}")
    WorksAuthorships[] getWorksAuthorships(String work_id);
    @Select("SELECT * FROM works_authorships WHERE work_id = #{work_id} and author_id = #{author_id}")
    WorksAuthorships getWorksAuthorshipsByAuthorId(String work_id,String author_id);
    @Select("SELECT display_name FROM authors WHERE id = #{authorId}")
    String getAuthorName(String authorId);
    @Select("SELECT * from works_locations where work_id = #{work_id}")
    WorksLocation[] getWorksLocation(String work_id);

    @Select("SELECT * from works_primary_locations where work_id = #{work_id}")
    WorksPrimaryLocations  getWorksPrimaryLocations(String work_id);

    @Select("SELECT * from works_referenced_works where work_id = #{work_id}")
    WorksReferencedWorks[] getWorksReferencedWorks(String work_id);

    @Select("SELECT * from authors_stats where author_id = #{authorId}")
    AuthorsStats  getAuthorsStats(String authorId);

    @Select("SELECT * FROM works_authorships WHERE author_id = #{authorId}")
    WorksAuthorships[] getPaper(String authorId);
    @Select("SELECT * FROM authors where id = #{authorId}")
    Authors getAuthors(String authorId);

    @Select("SELECT * from sources where id=#{id}")
    Sources getSources(String id);
    @Select("SELECT authors.display_name FROM works_locations\n" +
            "JOIN authors ON author_id = authors.id\n" +
            "WHERE work_id = #{work_id}")
    String[] joinName(String work_id);

//    @Select("select * from works_authorships where work_id=#{work_id}")
//    WorksAuthorships getAuthorships(String work_id);

    @Select("SELECT display_name from authors where id=#{authorId}")
    Authors getAuthor(String authorId);
}