package com.example.administration.controller;

import com.example.administration.model.shared.RestBean;
import com.example.administration.service.COSService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/file")
public class FileController {
    @Resource
    COSService cosService;

    @PostMapping("/upload")
    public ResponseEntity<RestBean> uploadFile(@RequestBody MultipartFile file) {
        // 获取文件的原始文件名
        String originalFilename = file.getOriginalFilename();
        // 获取文件的扩展名
        String fileExtension = getFileExtension(originalFilename);
        // 根据文件扩展名判断文件格式
        if (fileExtension != null) {
            String fileName = UUID.randomUUID()+"."+fileExtension;
            String url = cosService.upload(file,"file/"+fileName);
            return RestBean.success(url);
        } else {
            return RestBean.failure(HttpStatusCode.valueOf(403), "请指定拓展名");
        }
    }

    // 获取文件的扩展名
    private String getFileExtension(String filename) {
        String extension = null;
        if (filename != null && !filename.isEmpty()) {
            int dotIndex = filename.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < filename.length() - 1) {
                extension = filename.substring(dotIndex + 1).toLowerCase();
            }
        }
        return extension;
    }

}
