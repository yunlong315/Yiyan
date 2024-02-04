package com.example.backendusermanagement.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


@Data
public class UpdateProfileRequest {
    String username;//修改用户名
    String gender;
    String birthDate;
    String telephone;
    String educationAttainment;
    MultipartFile avatarFile;//用户头像
    String description;
}
