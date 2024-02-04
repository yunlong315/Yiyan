package com.example.administration.service;


import com.example.administration.config.COSConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;


@Service
public class COSService {
    @Value("${aliyun.bucketName}")
    private String bucketName;

    public String upload(MultipartFile file, String path){
        COSClient cosClient=new COSConfig().init();
        URL url = null;
        try{
            PutObjectResult res=cosClient.putObject(bucketName,path,file.getInputStream(),null);
            url=cosClient.getObjectUrl(bucketName,path);
            System.out.println(url);
        }catch ( Exception e){
            e.printStackTrace();
        }
        return String.valueOf(url) ;
    }
}
