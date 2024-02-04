package com.example.administration.mapper;
import com.example.administration.model.domain.ComplaintForm;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
@Mapper
public interface ComplaintMapper {
    //创建时create_time与update_time一致
    @Insert("INSERT INTO complaint_form (user_id, type, content, target_id, status, admin_id, reply, attachment_urls,create_time,update_time) " +
            "VALUES (#{userId}, #{type}, #{content}, #{targetId}, #{status}, #{adminId}, #{reply}, #{attachmentUrls},#{createTime},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addComplaint(ComplaintForm complaint);

    @Select("SELECT * FROM complaint_form WHERE user_id = #{userId}")
    List<ComplaintForm> getComplaintsByUserId(int userId);

    @Select("SELECT * FROM complaint_form WHERE id = #{id}")
    ComplaintForm getComplaint(int id);

    @Delete("DELETE  FROM complaint_form WHERE id = #{id}")
    boolean deleteComplaint(int id);


    @Select("<script>" +
            "SELECT * FROM complaint_form " +
            "WHERE admin_id = #{adminId} " +
            "<if test='type != null'>AND type = #{type}</if>" +
            "<if test='status != null'>AND status = #{status}</if>"+
            "</script>")
    List<ComplaintForm> getAdminComplaints(@Param("adminId") Integer adminId, @Param("type") Integer type, @Param("status") Integer status);

    @Update("UPDATE complaint_form SET " +
            "status = #{status}, " +
            "reply = #{reply}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    boolean updateComplaint(int id, int status, String reply, Date updateTime);

}