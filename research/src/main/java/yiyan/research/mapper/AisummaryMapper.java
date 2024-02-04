package yiyan.research.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import yiyan.research.model.domain.AiSummary;

@Mapper
public interface AisummaryMapper {
    @Select("select * from ai_summary where work_id = #{workId}")
    AiSummary getSummary(String workId);

    @Insert("INSERT INTO ai_summary (work_id, summary) VALUES (#{workId}, #{summary})")
    boolean addSummary(AiSummary aiSummary);

}
