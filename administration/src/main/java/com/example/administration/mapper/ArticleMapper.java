package com.example.administration.mapper;

import com.example.administration.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    @Insert("INSERT INTO article (name, admin_id, label, concepts, text, image_url ,create_time, update_time) " +
            "VALUES (#{name}, #{adminId}, #{label}, #{concepts}, #{text}, #{imageUrl},#{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Delete("DELETE FROM article WHERE id = #{id}")
    boolean deleteById(int id);

    @Select("SELECT * FROM article WHERE id = #{id}")
    Article findById(int id);

    @Update("<script>" +
            "UPDATE article" +
            "<set>" +
            "<if test='name != null'>name = #{name},</if>" +
            "<if test='label != null'>label = #{label},</if>" +
            "<if test='concepts != null'>concepts = #{concepts},</if>" +
            "<if test='text != null'>text = #{text},</if>" +
            "<if test='imageUrl != null'>text = #{image_url},</if>"+
            "update_time = #{updateTime}," +
            "</set>" +
            "WHERE id = #{id}" +
            "</script>")
    boolean update(Article article);

    @Select("<script>" +
            "SELECT * FROM article" +
            "<where>" +
            "<if test='name != null'>" +
            "AND name = #{name}" +
            "</if>" +
            "<if test='label != null'>" +
            "AND label = #{label}" +
            "</if>" +
            "</where>" +
            "</script>")
    List<Article> getArticles(@Param("name") String name, @Param("label") String label);

    @Insert({
            "<script>",
            "INSERT INTO article_work (article_id, work_id) VALUES",
            "<foreach item='relateId' collection='relateIds' separator=','>",
            "(#{articleId}, #{relateId})",
            "</foreach>",
            "</script>"
    })
    boolean relateToWorks(@Param("articleId") int articleId, @Param("relateIds") List<String> relateIds);

    @Delete("DELETE FROM article_work WHERE article_id = #{articleId}")
    void deleteOldRelations(@Param("articleId") int articleId);

    @Select("SELECT * FROM article_work WHERE article_id = #{articleId}")
    List<String> getRelations(int articleId);
}
