package com.example.backendusermanagement.model.request;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteItemRequest {
    private int favoriteIds;
    String workId;
}
