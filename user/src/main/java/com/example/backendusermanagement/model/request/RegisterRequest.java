package com.example.backendusermanagement.model.request;

import lombok.Data;

import java.beans.Transient;
import java.util.Date;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String avatar_url;
    private Date created_at;
    private boolean isActive=false;
    private String salt;

}
/*
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password1 VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(255),
    created_at DATETIME,
    isActive BOOLEAN DEFAULT false
);

 */
