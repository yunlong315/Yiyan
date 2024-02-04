package com.example.backendusermanagement.model.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Favorite {
    private int id;
    private  int userId;
    private String name;
    private Date createdAt;
    private Date updatedAt;
    private int idx=0;
    private boolean isPrivate=false;
    public Favorite(int userId,String name, Date createdAt, Date updatedAt, boolean isPrivate){
        this.userId=userId;
        this.name=name;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
//        this.idx=idx;
        this.isPrivate=isPrivate;
    }
    public Map<String,Object> toDict(){
        Map<String,Object>map=new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("createdAt",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        map.put("updatedAt",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updatedAt));
        return map;
    }
}
/*
CREATE TABLE IF NOT EXISTS Favorite (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    createdAt DATETIME NOT NULL,
    updatedAt DATETIME NOT NULL,
    `idx` INT NOT NULL DEFAULT 0,
    is_private TINYINT(1) NOT NULL DEFAULT 0
);
 */
