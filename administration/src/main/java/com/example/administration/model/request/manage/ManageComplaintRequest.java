package com.example.administration.model.request.manage;

import lombok.Data;

@Data
public class ManageComplaintRequest {
    private int id;
    private String reply;
    private int status;
}
