package com.example.backendusermanagement.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class FavoriteItem {
    private  int id;
    private int favoriteId;
    private int userId;
    private String workId;
    private Date createdAt;
//    public FavoriteItem(int favoriteId, int workId, Date createdAt) {
//        this.favoriteId = favoriteId;
//        this.workId = workId;
//        this.createdAt = createdAt;
//    }
    public Map<String,Object> toDict(){
        Map<String,Object>map=new HashMap<>();
        map.put("id",id);
        map.put("workId",workId);
        map.put("favoriteId",favoriteId);
        map.put("userId",userId);
        map.put("createdAt",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        return map;
    }
}
/*
CREATE TABLE IF NOT EXISTS FavoriteItem (
    id INT AUTO_INCREMENT PRIMARY KEY,
    favoriteId INT NOT NULL,
    userId INT NOT NULL,
    workId INT NOT NULL,
    createdAt DATETIME NOT NULL
);
 */
