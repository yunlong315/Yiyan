package com.example.administration.model.request.article;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateArticalRequest {
    private int id;
    private String name;
    private String label;
    private String concepts;
    private String text;
    private List<String> relateWorkIds;
    private String imageUrl;
}
