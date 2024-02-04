package com.example.administration.model.micro;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthorDeauthReceive {
    private String userId;
    private String authorId;
}

