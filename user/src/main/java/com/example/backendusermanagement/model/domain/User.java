package com.example.backendusermanagement.model.domain;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class User {
    private int id;
    private String username;
    private String password;

    private String email;
    private String gender;
    private Date birthDate;
    private String salt;
    private Date registerTime;
    private long telephone;
    private String educationAttainment;
    private boolean isActive;
    private String avatarUrl;
    private int authorId;
    private boolean isAdmin=false;
    private String description;


    public boolean is_active() {
        return isActive;
    }
    public Map<String,Object>toDict(){
//        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("username", username);
        map.put("email", email);
        map.put("gender", gender);
        map.put("birthDate", new SimpleDateFormat("yyyy-MM-dd").format(birthDate));
//        map.put("salt", salt);
        map.put("registerTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(registerTime));
        map.put("telephone", telephone);
        map.put("educationAttainment", educationAttainment);
//        map.put("isActive", isActive);
        map.put("avatarUrl", avatarUrl);
        map.put("authorId", authorId);
//        map.put("isAdmin", isAdmin);
        map.put("description",description);
        return map;
    }

}

/*
CREATE TABLE User (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(200),
    password VARCHAR(255),
    email VARCHAR(60),
    gender ENUM('male', 'female', 'other') NULL,
    birthDate DATE NULL,
    salt VARCHAR(30),
    registerTime DATETIME,
    telephone VARCHAR(20) NULL,
    nickname VARCHAR(200) NULL,
    educationAttainment ENUM('小学', '初中', '高中', '专科教育', '本科教育', '研究生教育') NULL,
    isActive BOOLEAN DEFAULT false,
    avatarUrl VARCHAR(200) NULL,
    isProfessional BOOLEAN NULL,
    researcherId INT NULL
);



 */
