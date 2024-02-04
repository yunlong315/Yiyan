package com.example.backendusermanagement.controller;


import com.example.backendusermanagement.model.RestBean;
import com.example.backendusermanagement.model.domain.User;
import com.example.backendusermanagement.model.request.*;
import com.example.backendusermanagement.service.*;
import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserControler {
    @Resource
    ManageService manageService;
    @Resource
    EmailService emailService;
    @Resource
    COSService cosService;
    @Resource
    FavoriteService favoriteService;
    @Resource
    MessageService messageService;

    @PostMapping("/register")
    public ResponseEntity<RestBean<String>> register(@RequestBody RegisterRequest registerRequest, HttpServletRequest httpServletRequest) throws TemplateException, IOException, MessagingException {
        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String salt = registerRequest.getSalt();

        int res = manageService.register(username, password, email, salt);


//        if (res == -1) {
//            return RestBean.failure(HttpStatus.EXPECTATION_FAILED, "用户名不得长于20位");
//        } else if (res == -2) {
//            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "密码不合法，必须是大写/小写字母，数字，6-16位");
//        } else if (res == -4) {
//            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "邮箱不合法");
//        } else if (res == -5) {
//            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "哈哈，太急了吧");
//        } else if (res == -6) {
//            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "亲，账号已被注册喔");
//        }
        User user = manageService.getUserByEmail(email);
        String token = manageService.generateToken(user.getId());
        boolean debug = System.getProperty("os.name").toLowerCase().contains("win");

        String url = debug ? "http://127.0.0.1:8080/user/activate/" + token : "http://www.aamofe.top/user/activate/" + token;
        Map<String, String> model = new HashMap<>();
        model.put("url", url);
        model.put("username", username);

        boolean emailSent = emailService.sendEmail1(email, "账户激活", "register", model);

        if (emailSent) {
            System.out.println(url);
            return RestBean.success("注册成功，请查收验证邮件");
        } else {
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "注册失败，邮件发送异常");
        }
    }

    @PostMapping("/generateCode")
    public ResponseEntity<RestBean<String>> getCode(HttpServletRequest httpServletRequest) throws MessagingException, TemplateException, IOException {
        User user = (User) httpServletRequest.getAttribute("user");
        int code = manageService.generateVerificationCode();
        Map<String, String> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("code", String.valueOf(code));
        model.put("type", "1");
        model.put("userId", String.valueOf(user.getId()));
        boolean emailSent = emailService.sendEmail1(user.getEmail(), "确认账户", "changePswd", model);
        if (emailSent) {
            manageService.saveCode(model);
            return RestBean.success("验证码发送成功，请查收");
        } else {
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "邮件发送异常");
        }
        /*
        用户id，type（1是找回密码），code
         */

    }
    @GetMapping("/getSalt")
    public ResponseEntity<RestBean<String>> getSalt(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        return RestBean.success(user.getSalt());
    }
    @PostMapping("changePassword")
    public ResponseEntity<RestBean<String>> changePassword(@RequestBody ChangPasswordRequest changPasswordRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        boolean res = manageService.changePassword(user, changPasswordRequest.getCode(), changPasswordRequest.getNewPassword());
        if (res) {
            return RestBean.success("密码修改成功");
        }
        return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "验证码错误");
    }

    @GetMapping("/activate/{token}")
    public ModelAndView activate(@PathVariable String token) {
        int res = emailService.activate(token);
        Map<String, String> model = new HashMap<>();
        String subject, url, text;

        if (res != 0) {
            subject = "注册失败";
            url = "/login";

            if (res == -1)
                text = "链接已过期，点击下方链接重新注册";
            else
                text = "token解析失败，点击下方链接重新注册";

//            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, text);
        } else {
            subject = "注册成功";
            url = "/index";
            text = "注册成功，点击下方链接登录~";
//            return RestBean.success(text);
        }
        model.put("subject", subject);
        model.put("url", url);
        model.put("text", text);
        final ModelAndView response = new ModelAndView("activate");
        response.addAllObjects(model);
        return response;
    }

    @PostMapping("/login")
    public ResponseEntity<RestBean<Map<String,Object>>> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        int res = manageService.getUserByPassword(email, password);
        if (res == 0) {
            User user = manageService.getUserByEmail(email);
            Map<String,Object>map=user.toDict();
            String token = manageService.generateToken(user.getId());
            map.put("token",token);
            map.put("isAdmin",user.isAdmin());
            return RestBean.success(map);
        } else if (res == -1) {
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
    @GetMapping("/profileById")
    public ResponseEntity<RestBean<Map>> getUserProfileById(@RequestParam int userId) {
        User user = manageService.getUserById(String.valueOf(userId));
        if (user == null)
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, null);
        return RestBean.success(user.toDict());
    }
    @GetMapping("/profile")
    public ResponseEntity<RestBean<Map>> getUserProfile(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        if (user == null)
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, null);
        return RestBean.success(user.toDict());
    }

    @PostMapping("/update")//修改个人信息
    public ResponseEntity<RestBean<String>> updateProfile(UpdateProfileRequest req, HttpServletRequest httpServletRequest) throws ParseException {
        boolean updateSuccess = false;
        MultipartFile avatarFile = req.getAvatarFile();
        String username = req.getUsername();
        User user = (User) httpServletRequest.getAttribute("user");
//        System.out.println(username);
        if (avatarFile != null) {
            String fileName = UUID.randomUUID() + ".png";
            updateSuccess = cosService.uploadAvatar(avatarFile, "user-avatar/" + fileName, user);
        }
        //username、gender、birthDate、telephone、nickname、educationAttainment

        boolean res=manageService.updateProfile(user.getId(),username,req.getGender(),req.getBirthDate(),req.getTelephone(),req.getEducationAttainment(),req.getDescription());
        if (updateSuccess||res) {
            return RestBean.success("修改个人信息成功");
        } else {
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "修改个人信息失败");
        }
    }

    //创建收藏夹
    @PostMapping("/createFavorite")
    public ResponseEntity<RestBean<Map>> createFavorite(@RequestBody CreateFavoriteRequest createFavoriteRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String name = createFavoriteRequest.getFavoriteName();

        int favoriteId = favoriteService.createFavorite(user.getId(), name);
        if (favoriteId != -1) {
            Map<String, Object> map = new HashMap<>() {{
                put("id", favoriteId);
            }};
            return RestBean.success(map);
        }
        return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    //收藏文献
    //我需要判断文献是否存在吗
    @PostMapping("/favoriteItem")
    public ResponseEntity<RestBean<String>> favoriteItem(FavoriteItemRequest favoriteItemRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
//        System.out.println("user: " + user.getId() + " favoriteItem" + favoriteItemRequest.getWorkId());
        int res=favoriteService.favoriteItem(user.getId(), favoriteItemRequest.getFavoriteIds(), favoriteItemRequest.getWorkId());
        if(res==-1)
            return RestBean.success("收藏夹不存在");
        else if(res==-2)
            return RestBean.success("已收藏");
        else
            return RestBean.success("收藏成功");
    }

    @PostMapping("/cancelFavoriteItem")
    public ResponseEntity<RestBean<String>> cancelFavoriteItem(FavoriteItemRequest favoriteItemRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
//        System.out.println("user: " + user.getId() + " favoriteItem" + favoriteItemRequest.getWorkId());
        int res=favoriteService.cancelFavoriteItem(user.getId(), favoriteItemRequest.getFavoriteIds(), favoriteItemRequest.getWorkId());
        System.out.println("res: " + res);
        if(res==-1)
            return RestBean.success("收藏夹不存在");
        else if(res==-2)
            return RestBean.success("已取消收藏");
        else
            return RestBean.success("取消收藏成功");
    }


    //取消收藏

    //查看所有收藏夹
    @GetMapping("/getAllFavorite")
    public ResponseEntity<RestBean<List<Map<String, Object>>>> getAllFavorite(@RequestParam String workId,HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        System.out.println("你好nihao ");
        List<Map<String, Object>> res = favoriteService.getAllFavorite(user.getId(),workId);
        return RestBean.success(res);
    }
    //查看收藏夹的所有记录

    @GetMapping("/getAllFavoriteItem")
    public ResponseEntity<RestBean<List<Map<String, Object>>>> getAllFavoriteItem(@RequestParam int favoriteId, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        List<Map<String, Object>> res = favoriteService.getAllFavoriteItem(user.getId(), favoriteId);
        System.out.println("favoriteId :" + favoriteId);
        return RestBean.success(res);
    }

    @PostMapping("/updateFavorite")
    public ResponseEntity<RestBean<String>> updateFavorite(@RequestBody UpdateFavorite updateFavorite, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        String name = updateFavorite.getName();
        boolean isPrivate = updateFavorite.isPrivate();
        boolean delete = updateFavorite.isDelete();
        boolean res = favoriteService.updateFavorite(user.getId(), updateFavorite.getFavoriteId(), name, isPrivate, delete);
        if (res)
            return RestBean.success("修改信息成功");
        else
            return RestBean.success("修改信息失败");

    }

    @GetMapping("/getAllMessage")
    public ResponseEntity<RestBean<List<Map<String, Object>>>> getAllMessage(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");
        List<Map<String, Object>> res = messageService.getAllMessage(user.getId());
//        System.out.println("favoriteId :" + favoriteId);
        return RestBean.success(res);
    }

    @PostMapping("/readMessage")
    public ResponseEntity<RestBean<String>> readMessage(@RequestBody ReadMessageRequest readMessageRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");

        boolean res = messageService.readMessage(user.getId(), readMessageRequest.getMessageId());
        if (res)
            return RestBean.success("已读成功");
        else
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "已读失败");
    }

    @PostMapping("/deleteMessage")
    public ResponseEntity<RestBean<String>> deleteMessage(@RequestBody ReadMessageRequest readMessageRequest, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");

        boolean res = messageService.deleteMessage(user.getId(), readMessageRequest.getMessageId());
        if (res)
            return RestBean.success("删除成功");
        else
            return RestBean.failure(HttpStatus.INTERNAL_SERVER_ERROR, "删除失败");
    }

    @PostMapping("/readAllMessage")
    public ResponseEntity<RestBean<String>> readAllMessage(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getAttribute("user");

        messageService.readAllMessage(user.getId());
        return RestBean.success("全部已读");
    }


/*
获取所有消息  删除消息 一键已读消息 创建消息（消息队列）
 */


}
