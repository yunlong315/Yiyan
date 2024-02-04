package com.example.administration.model.micro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckAdminReply {
    private int id;
    private boolean isAdmin;
}
