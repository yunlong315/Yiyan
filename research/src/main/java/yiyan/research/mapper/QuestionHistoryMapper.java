package yiyan.research.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import yiyan.research.model.domain.QuestionHistory;

@Mapper
public interface QuestionHistoryMapper {

    @Insert("INSERT INTO question_history (work_id, user_id, history) VALUES (#{workId}, #{userId}, #{history})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    boolean insertQuestionHistory(QuestionHistory questionHistory);

    @Select("SELECT * FROM question_history WHERE work_id=#{workId} and user_id=#{userId}")
    QuestionHistory getQuestionHistory(@Param("workId") String workId,@Param("userId") int userId);

    @Update("UPDATE question_history SET work_id = #{workId}, user_id = #{userId}, history = #{history} WHERE id = #{id}")
    boolean updateQuestionHistory(QuestionHistory questionHistory);

    @Delete("DELETE FROM question_history WHERE id = #{id}")
    void deleteQuestionHistory(@Param("id") int id);
}
