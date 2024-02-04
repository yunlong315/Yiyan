package com.example.administration.model.request.manage;

import lombok.Data;

@Data
public class ManageRequestRequest {
    private int id;
    private String reply;
    private int status;
}
