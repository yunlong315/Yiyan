package com.example.backendusermanagement.mapper;

import com.example.backendusermanagement.model.domain.Code;
import com.example.backendusermanagement.model.domain.User;
import org.apache.ibatis.annotations.*;

import java.util.Date;


@Mapper
public interface UserMapper {
    @Insert("INSERT INTO User (username, email, password, registerTime,salt) VALUES (#{username}, #{email}, #{password}, #{registerTime}, #{salt})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertUser(User user);

    @Select("SELECT * FROM User WHERE email=#{email} ORDER BY id DESC LIMIT 1")
    User getUserByEmail(String email);

    @Select("SELECT * FROM User WHERE id=#{id} ORDER BY id DESC LIMIT 1 ")
    User getUserById(String id);

    @Update("UPDATE User SET isActive = true WHERE id = #{id}")
    void updateUser(User user);
    @Update(("UPDATE User SET authorId = #{auhtorId} WHERE id = #{id}"))
    void updateAuthorId(String authorId,int id);
    @Delete("DELETE FROM User WHERE email = #{email} AND isActive = false")
    void deleteInactiveUser(String email);
    @Update("UPDATE User SET avatarUrl=#{avatarUrl, jdbcType=VARCHAR}  WHERE id = #{id}")
    void updateAvatarUrl(String avatarUrl,int id);


    @Insert("INSERT INTO Code (userId, type, code,createdAt) VALUES (#{userId}, #{type},#{code}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertCode(Code code);

    @Update("UPDATE User SET password =#{newPassword} WHERE id = #{id}")
    void updatePassword(int id,String newPassword);
    @Select("SELECT * FROM Code WHERE userId = #{userId} and code=#{code} ")
    Code getCode(int userId,int code);

    @Select("SELECT * FROM Code WHERE userId = #{userId} and code=#{code} and email = #{email} ")
    Code getCode1(int userId,int code,String email);
    @Update("UPDATE Code SET used=#{used} WHERE id = #{id}")
    void updateCode(int id,boolean used);

    @Update("UPDATE User set username =#{name} where id=#{id}")
    void changeName(String name,int id);


    @Update("UPDATE User SET gender = #{gender} WHERE id = #{id}")
    void changeGender(String gender, int id);

    @Update("UPDATE User SET birthDate = #{birthDate} WHERE id = #{id}")
    void changeBirthDate(Date birthDate, int id);

    @Update("UPDATE User SET telephone = #{telephone} WHERE id = #{id}")
    void changeTelephone(String telephone, int id);

    @Update("UPDATE User SET educationAttainment = #{educationAttainment} WHERE id = #{id}")
    void changeEducationAttainment(String educationAttainment, int id);

    @Update("UPDATE User SET description = #{description} WHERE id = #{id}")
    void changeDescription(String description, int id);

    //关注者数量
}
