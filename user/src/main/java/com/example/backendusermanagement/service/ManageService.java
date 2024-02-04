package com.example.backendusermanagement.service;

import com.example.backendusermanagement.model.domain.User;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public abstract class ManageService {
    public abstract void authenticate(int userId,String authorId);
    public abstract boolean getCode(int userId,int code1,String email);
//    public abstract User register(String name,String email,String password,String avatar_url);
    public abstract int register(String username,String password,String email,String salt);
    public abstract String generateToken(int id);
    public abstract User getUserByEmail(String email);
    public abstract int getUserByPassword(String email,String password);

    public abstract User getUserById(String id);
    public abstract int generateVerificationCode();
    //
    public abstract int  getIdByToken(String token);

    public abstract void saveCode(Map<String ,String> model);

    public abstract boolean changePassword(User user,int code,String newPassword);
    public abstract boolean updateProfile(int userId, String username, String gender, String birthDate, String telephone, String educationAttainment,String description) throws ParseException;
}
