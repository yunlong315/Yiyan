package com.example.backendusermanagement.service.serviceImp;

import com.example.backendusermanagement.mapper.FavoriteMapper;
import com.example.backendusermanagement.model.domain.Favorite;
import com.example.backendusermanagement.model.domain.FavoriteItem;
import com.example.backendusermanagement.producer.Producer;
import com.example.backendusermanagement.service.FavoriteService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class FavoriteServiceImp extends FavoriteService {
    @Resource
    FavoriteMapper favoriteMapper;

    @Resource
    Producer producer;
    @Override
    public int createFavorite(int userId,String name) {
        System.out.println("222 userid : "+userId+" "+name);
//        Favorite favorite =favoriteMapper.getFavoriteByName(userId,name);
//        System.out.println("111  "+favorite);
//        if(favorite !=null)
//            return -1;
        Date created_at = new Date();
        Date updated_at = new Date();
        //int userId,String name, Date createdAt, Date updatedAt, boolean isPrivate
        Favorite favorite =new Favorite(userId,name,created_at, updated_at,false);
        try {
            favoriteMapper.insertFavorite(favorite);

        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("333 userid : "+userId+" "+name);
        Favorite f =favoriteMapper.getFavoriteByName(userId,name);
        System.out.println("444 userid : "+userId+" "+name);
        return f.getId();
    }
    //s收藏文献
    @Override
    public int favoriteItem(int userId, int favoriteId, String workId) {
        Favorite favorite =favoriteMapper.getFavoriteById(userId,favoriteId);
        if(favorite==null){//收藏夹不存在
            return -1;
        }
        FavoriteItem favoriteItem=favoriteMapper.getFavoriteItemByFW(favoriteId,workId);
        if(favoriteItem!=null)//已收藏
            return -2;
        Date created_at = new Date();
        favoriteMapper.updateFavoriteTime(new Date(),favoriteId);
        FavoriteItem item = new FavoriteItem();
        item.setFavoriteId(favoriteId);
        item.setWorkId(workId);
        item.setCreatedAt(created_at);
        item.setUserId(userId);
        favoriteMapper.insertFavoriteItem(item);
        return 0;
//        for(int favoriteId:favoriteIds){
//            Favorite favorite =favoriteMapper.getFavoriteById(userId,favoriteId);
//            if(favorite==null){//收藏夹不存在
//                continue;
//            }
//            FavoriteItem favoriteItem=favoriteMapper.getFavoriteItemByFW(favoriteId,workId);
//            if(favoriteItem!=null)//已收藏
//                continue;
//            Date created_at = new Date();
//            favoriteMapper.updateFavoriteTime(new Date(),favoriteId);
//            FavoriteItem item = new FavoriteItem();
//            item.setFavoriteId(favoriteId);
//            item.setWorkId(workId);
//            item.setCreatedAt(created_at);
//            item.setUserId(userId);
//            favoriteMapper.insertFavoriteItem(item);
//            System.out.println("你好："+favoriteId);
//        }
//
//        int [] previous=favoriteMapper.getFavoriteByWorkId(workId);
//        for(int previousId:previous){
//            if(!favoriteIds.contains(previousId)) {
//                FavoriteItem item = favoriteMapper.getFavoriteItemByFW(previousId, workId);
//                if(item != null) {
//                    favoriteMapper.deleteFavoriteItem(item.getId());
//                    favoriteMapper.updateFavoriteTime(new Date(),previousId);
//                }
//            }
//        }
//        return 0;
    }

    @Override
    public int cancelFavoriteItem(int userId, int favoriteId, String workId) {
        Favorite favorite =favoriteMapper.getFavoriteById(userId,favoriteId);
        if(favorite==null){//收藏夹不存在
            return -1;
        }
        FavoriteItem favoriteItem=favoriteMapper.getFavoriteItemByFW(favoriteId,workId);
        if(favoriteItem==null)//已收藏
            return -2;
        favoriteMapper.deleteFavoriteItemByFW(favoriteId,workId);
        return 0;
    }

    //修改收藏夹
    public boolean updateFavorite(int userId,int favoriteId,String name,boolean isPrivate,boolean delete){
        Favorite favorite =favoriteMapper.getFavoriteById(userId,favoriteId);
        if(favorite == null)
            return false;
        if(delete){
            favoriteMapper.deleteFavoriteItem(favoriteId);
            return true;
        }
        if(name!=null){
            favoriteMapper.updateName(favoriteId,name);
            favoriteMapper.updateFavoriteTime(new Date(),favoriteId);
            return true;
        }
        if(isPrivate){
            favoriteMapper.updateFavoriteStatus(favoriteId,isPrivate);
            favoriteMapper.updateFavoriteTime(new Date(),favoriteId);
            return true;
        }
        return false;
    }

    @Override
    public boolean isFavorite(int userId, String workId) {
        //true是已收藏
        return favoriteMapper.isFavorite(userId,workId)!=null;
    }



    @Override
    public List<Map<String, Object>> getAllFavorite(int userId,String workId) {
        Favorite [] favorites = favoriteMapper.getFavoriteByUserId(userId);
        List<Map<String, Object>> list = new ArrayList<>();

        for(Favorite favorite:favorites){
            Map<String, Object> m=favorite.toDict();
            int count=(favoriteMapper.getFavoriteItemByFavoriteId(favorite.getId())).length;
            m.put("count",count);
            boolean isFavorite=false;
            if(workId!=null){
                isFavorite=favoriteMapper.getFavoriteItemByFW(favorite.getId(),workId)!=null;
            }
            m.put("isFavorite",isFavorite);
            list.add(m);
        }
        return list;
    }

    //获取单个收藏夹的所有内容
    @Override
    public List<Map<String, Object>> getAllFavoriteItem(int userId,int favoriteId) {
        /*
        [ {"id":id,"name":name},{"id":id,"name":name}...,{"id":id,"name":name}];
         消息队列的接口先留着
         */
        FavoriteItem [] favoriteItems=favoriteMapper.getFavoriteItemByFavoriteId(favoriteId);
//        System.out.println(favoriteId);
        List<Map<String, Object>> list = new ArrayList<>();

        System.out.println("list length: " + favoriteItems.length);
        for(FavoriteItem favoriteItem:favoriteItems){
            Map<String,Object>o =producer.syncSend(new HashMap<>(){{
                put("type",4);
                put("workId",favoriteItem.getWorkId());
            }});
            for(String s:o.keySet()){
                System.out.println("键值对 "+s+" : "+o.get(s));
            }
            list.add(o);
        }
        return list;
    }
    //关注科研人员 查看科研人员列表 取消关注
    //修改手粗按的名称
}
