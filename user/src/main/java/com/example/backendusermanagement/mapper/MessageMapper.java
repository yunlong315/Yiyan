package com.example.backendusermanagement.mapper;

import com.example.backendusermanagement.model.domain.Favorite;
import com.example.backendusermanagement.model.domain.Message;
import org.apache.ibatis.annotations.*;

@Mapper
public interface  MessageMapper {

    @Select("select * from Message where dstId=#{dstId}")
    Message[] getAllMessage(int dstId) ;

    @Select("select * from Message where id=#{id}")
    Message getMessageById(int id);

    @Update("update Message set isRead=#{isRead} where id=#{id}")
    void readMessage(int id);

    @Delete("DELETE FROM Message WHERE id = #{id}")
    void deleteMessage(int id);

    @Update("update Message set isRead=true where dstId=#{dstId}")
    void readAllMessage(int id);


    @Insert("INSERT INTO Message (dstId, type, content, createdAt,  srcId) " +
            "VALUES (#{dstId}, #{type}, #{content}, #{createdAt},  #{srcId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertMessage(Message m);
}
