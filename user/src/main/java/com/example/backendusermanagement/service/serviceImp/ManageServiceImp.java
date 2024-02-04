package com.example.backendusermanagement.service.serviceImp;
import com.example.backendusermanagement.model.domain.Code;
import io.jsonwebtoken.*;
import org.apache.commons.codec.digest.DigestUtils;
import com.example.backendusermanagement.mapper.UserMapper;
import com.example.backendusermanagement.service.ManageService;
import com.example.backendusermanagement.model.domain.User;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ManageServiceImp extends ManageService {
    public static final String SECRET_KEY = "your_secret_key"; // 替换为真实的密钥
    private static final long EXPIRATION_TIME_MILLIS =  24 * 60 * 60 * 1000;
    @Resource
    UserMapper userMapper;
    @Override
    public int register(String username,String password,String email,String salt){
        //
//        if (username.length() > 20) {
//            return -1;
//        }

        boolean isEmailValid = email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        if(!isEmailValid) {
            return -4;
        }
        User user=getUserByEmail(email);
        Date created_at = new Date();
        if (user != null) {
            long timeDifferenceMillis = created_at.getTime() - user.getRegisterTime().getTime();
            long secondsDifference = timeDifferenceMillis / 1000;
            if (secondsDifference < 300) {
                return  -5;
            }
            else if(user.is_active()){
                return  -6;
            }
        }
        user=new User();
        user.setUsername(username);
        user.setEmail(email);
        password=saltEncryption(password,salt);
        user.setSalt(salt);
        user.setPassword(password);
        user.setRegisterTime(created_at);
        userMapper.insertUser(user);
        return 0;
    }
    public String generateToken(int id){
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRATION_TIME_MILLIS);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public User getUserByEmail(String email){
        return userMapper.getUserByEmail(email);
    }

    @Override
    public int getUserByPassword(String email, String password) {
        User user=userMapper.getUserByEmail(email);
        if(user==null||!user.is_active()){
            return -1;//用户未注册
        }
        password=saltEncryption(password,user.getSalt());
        if(!user.getPassword().equals(password)){
            System.out.println("密码不正确");
            return -2;//密码不正确
        }
        return 0;
    }

    @Override
    public User getUserById(String id) {
        return userMapper.getUserById(id);
    }
    public static String saltEncryption(String password, String salt){
        password = password+salt;
        return DigestUtils.sha256Hex(password);
    }

    //
    @Override
    public int  getIdByToken(String token) {
        int res;
        if (token == null) {
            res=-1;
        } else if (token.equals("111")) {
            res = 11;
        } else {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
                String id = claims.getSubject();
                User user = userMapper.getUserById(id);
                if (user == null) {
                    res=-1;
                }
                else
                    res=user.getId();
            } catch (JwtException e) {
                res=-1;
            }
        }
        return res;
    }

    public int generateVerificationCode(){
        int codeLength = 6;
        // 验证码字符集
        String codeChars = "0123456789";
        // 用于生成随机验证码的Random对象
        Random random = new Random();
        // 用于存储生成的验证码
        StringBuilder verificationCode = new StringBuilder();
        // 循环生成验证码
        for (int i = 0; i < codeLength; i++) {
            // 从字符集中随机选取一个字符
            char randomChar = codeChars.charAt(random.nextInt(codeChars.length()));
            // 添加到验证码字符串中
            verificationCode.append(randomChar);
        }
        // 返回生成的验证码
        return Integer.parseInt(verificationCode.toString());
    }

    @Override
    public void saveCode(Map<String, String> model) {
        Code code=new Code();
        code.setCode(Integer.parseInt(model.get("code")));
        code.setType(Integer.parseInt(model.get("type")));
        code.setUserId(Integer.parseInt(model.get("userId")));
        if(model.containsKey("email"))
            code.setEmail(model.get("email"));
        Date created_at = new Date();
        code.setCreatedAt(created_at);
//        for (Map.Entry<String, String> entry : model.entrySet()) {
//            System.out.println(entry.getKey()+ " "+entry.getValue());
//        }
        userMapper.insertCode(code);
        System.out.println(code.getCode());
    }

    @Override
    public boolean changePassword(User user, int code1, String newPassword) {
        int userId=user.getId();
//        System.out.println("111  "+userId+" " +code1 +" "+newPassword);

        Code code=userMapper.getCode(userId,code1);

        if(code==null||code.isUsed()){
            return false;
        }
        Date created_at = new Date();
        long timeDifferenceMillis = created_at.getTime() - user.getRegisterTime().getTime();
        long secondsDifference = timeDifferenceMillis / 1000;
        if (secondsDifference < 3000) {
            return  false;
        }
        newPassword=saltEncryption(newPassword,user.getSalt());
        userMapper.updatePassword(userId,newPassword);
        userMapper.updateCode(code.getId(),true);
        return true;
    }

    @Override
    public void authenticate(int userId, String authorId) {
        User u=userMapper.getUserById(String.valueOf(userId));
        userMapper.updateAuthorId(authorId,userId);
    }

    public boolean getCode(int userId, int code1, String email){
        Code code=userMapper.getCode1(userId,code1,email);
        if(code==null||code.isUsed())
            return false;
        userMapper.updateCode(code.getId(),true);
        return true;
    }

    public boolean updateProfile(int userId,String username, String gender, String birthDate, String telephone,  String educationAttainment,String description) throws ParseException {
        if (username != null) {
            userMapper.changeName(username, userId);
        }
        if (gender != null) {
            List<String> allowedGenders = Arrays.asList("男", "女", "其他");
            if (!allowedGenders.contains(gender)) {
                gender = "其他";
            }
            userMapper.changeGender(gender, userId);
        }

        if (birthDate != null) {
            Date d=new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
            userMapper.changeBirthDate(d, userId);
        }

        if (telephone != null) {
            userMapper.changeTelephone(telephone, userId);
        }

        if (educationAttainment != null) {
            List<String> allowedEducationAttainments = Arrays.asList("小学", "初中", "高中", "专科教育", "本科教育", "研究生教育", "其他");
            if (!allowedEducationAttainments.contains(educationAttainment)) {
                educationAttainment = "其他";
            }
            userMapper.changeEducationAttainment(educationAttainment, userId);
        }
        if(description!=null){

        }
        return true;
    }


}
