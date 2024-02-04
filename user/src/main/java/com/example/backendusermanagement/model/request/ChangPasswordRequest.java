package com.example.backendusermanagement.model.request;

import lombok.Data;

@Data
public class ChangPasswordRequest {
    int code;
    String newPassword;
}
