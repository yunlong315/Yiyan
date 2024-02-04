package com.example.backendusermanagement.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Code {
    private int id;
    private int userId;
    private int type;
    private int code;
    private Date createdAt;
    private String email;
    private boolean used=false;
}
/*
CREATE TABLE Code (
    id INT PRIMARY KEY,
    userId VARCHAR(255),
    type VARCHAR(255),
    code VARCHAR(255),
    createdAt DATE
);

 */
