package com.example.administration.mapper;

import com.example.administration.model.domain.RequestForm;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;

@Mapper
public interface RequestMapper {
    // 创建时createTime与updateTime一致
    @Insert("INSERT INTO request_form (user_id, type, content, target_id, status, admin_id, reply, attachment_urls,create_time,update_time) " +
            "VALUES (#{userId}, #{type}, #{content}, #{targetId}, #{status}, #{adminId}, #{reply}, #{attachmentUrls},#{createTime},#{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addRequest(RequestForm request);

    @Select("SELECT * FROM request_form WHERE user_id = #{userId}")
    List<RequestForm> getRequestsByUserId(int userId);

    @Select("SELECT * FROM request_form WHERE id = #{id}")
    RequestForm getRequest(int id);


    @Select("<script>" +
            "SELECT * FROM request_form " +
            "WHERE admin_id = #{adminId} " +
            "<if test='type != null'>AND type = #{type}</if>" +
            "<if test='status != null'>AND status = #{status}</if>"+
            "</script>")
    List<RequestForm> getAdminRequests(@Param("adminId") Integer adminId, @Param("type") Integer type, @Param("status") Integer status);

    @Update("UPDATE request_form SET " +
            "status = #{status}, " +
            "reply = #{reply}, " +
            "update_time = #{updateTime} " +
            "WHERE id = #{id}")
    boolean updateRequest(int id, int status, String reply, Date updateTime);

}