package com.example.backendusermanagement.service;

import java.util.List;
import java.util.Map;

public abstract class FavoriteService {
    public abstract int createFavorite(int userId,String name);
    public abstract int favoriteItem(int userId, int favoriteId, String workId);
    public abstract int cancelFavoriteItem(int userId, int favoriteId, String workId);

    public abstract List<Map<String, Object>> getAllFavorite(int userId,String workId);
    public abstract List<Map<String, Object>> getAllFavoriteItem(int userId,int favoriteId);
    public abstract boolean updateFavorite(int userId,int favoriteId,String name,boolean isPrivate,boolean delete);

    public abstract boolean isFavorite(int userId,String workId);
}
