package com.example.backendusermanagement.service;

import com.example.backendusermanagement.mapper.UserMapper;
import com.example.backendusermanagement.model.domain.User;
import com.qcloud.cos.COSClient;
import com.example.backendusermanagement.config.COSConfig;
import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URL;
import java.util.Objects;

@Service
public class COSService {
    @Value("${aliyun.bucketName}")
    private String bucketName;
    @Resource
    UserMapper userMapper;
    //上传视频
    //上传音频
    //上传zip
    public boolean uploadAvatar(MultipartFile file, String path, User user){
        COSClient cosClient=new COSConfig().init();
        try{
            PutObjectResult res=cosClient.putObject(bucketName,path,file.getInputStream(),null);
            URL url=cosClient.getObjectUrl(bucketName,path);
            userMapper.updateAvatarUrl(String.valueOf(url),user.getId());
            System.out.println("url :"+url);
            return true;
        }catch ( Exception e){
            e.printStackTrace();
        }
        return false;
    }
//    public boolean upload(MultipartFile file, String path){
//        COSClient cosClient=new COSConfig().init();
//        try{
//            PutObjectResult res=cosClient.putObject(bucketName,path,file.getInputStream(),null);
//            URL url=cosClient.getObjectUrl(bucketName,path);
//            System.out.println(String.valueOf(url));
//        }catch ( Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }
}
