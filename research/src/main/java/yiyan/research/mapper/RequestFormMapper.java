package yiyan.research.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import yiyan.research.model.domain.RequestForm;

@Mapper
public interface RequestFormMapper {
    @Insert("INSERT INTO request_form (user_id, type, content, author_id, work_id, status, admin_id, reply, create_time, update_time, attachment_url) " +
                "VALUES (#{userId}, #{type}, #{content}, #{authorId}, #{workId}, #{status}, #{adminId}, #{reply}, #{createTime}, #{updateTime}, #{attachmentUrl})")
    int insertRequestForm(RequestForm requestForm);
}
