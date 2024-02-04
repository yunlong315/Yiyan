package com.example.backendusermanagement.model.request;

import lombok.Data;

@Data
public class UpdateFavorite {
    private String name;
    private int favoriteId;
    private boolean isPrivate;
    private boolean delete;
}
