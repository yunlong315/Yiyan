package com.example.backendusermanagement.mapper;

import com.example.backendusermanagement.model.domain.Favorite;
import com.example.backendusermanagement.model.domain.FavoriteItem;
import lombok.Data;
import org.apache.ibatis.annotations.*;

import java.util.Date;

@Mapper
public interface FavoriteMapper {
    @Insert("INSERT INTO Favorite (userId, name, createdAt, updatedAt, isPrivate) " +
            "VALUES (#{userId}, #{name}, #{createdAt}, #{updatedAt}, #{isPrivate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertFavorite(Favorite favorite);

    @Select("SELECT * FROM Favorite WHERE userId = #{userId} AND name = #{name} ORDER BY id DESC LIMIT 1")
    Favorite getFavoriteByName(int userId, String name);

    @Select("SELECT * FROM Favorite WHERE  userId = #{userId} AND id=#{id}")
    Favorite getFavoriteById(int userId, int id);

    @Insert("INSERT INTO FavoriteItem (favoriteId, workId, createdAt,userId) " +
            "VALUES (#{favoriteId}, #{workId}, #{createdAt},#{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insertFavoriteItem(FavoriteItem favoriteItem);

    @Delete("DELETE FROM FavoriteItem WHERE favoriteId = #{favoriteId} and workId =#{workId}")
    void deleteFavoriteItemByFW(int favoriteId, String workId);
    //查找收藏文献的所有收藏夹
    @Select("SELECT favoriteId FROM FavoriteItem WHERE workId = #{workId}")
    int [] getFavoriteByWorkId(String workId);
    
    //查找当前用户的所有收藏夹
    @Select("SELECT * FROM Favorite WHERE userId = #{userId}")
   Favorite [] getFavoriteByUserId(int userId);
    //获取指定收藏夹的记录
    @Select("SELECT * FROM FavoriteItem " +
            "WHERE favoriteId = #{favoriteId} AND workId = #{workId}")
    FavoriteItem getFavoriteItemByFW(int favoriteId,String workId);

    @Select("SELECT * FROM FavoriteItem " +
            "WHERE id = #{id}")
    FavoriteItem getFavoriteItemById(int id);
    @Delete("DELETE FROM FavoriteItem WHERE id = #{id}")
    void deleteFavoriteItem(int id);

    @Select("SELECT * FROM FavoriteItem " +
            "WHERE favoriteId = #{favoriteId} ")
    FavoriteItem[] getFavoriteItemByFavoriteId(int favoriteId);

    @Update("UPDATE FavoriteItem SET isPrivate=#{isPrivate} WHERE id = #{id}")
    void updateFavoriteStatus(int id,boolean isPrivate);

    @Update("UPDATE FavoriteItem SET name=#{name}  WHERE id = #{id}")
    void updateName(int id,String name);

    @Update("UPDATE Favorite SET updatedAt=#{date}  WHERE id = #{favoriteId}")
    void updateFavoriteTime(Date date,int favoriteId);

    @Select("SELECT * FROM FavoriteItem where favoriteId = #{favoriteId} and userId = #{userId}")
    FavoriteItem isFavorite(int favoriteId,String userId);
}
