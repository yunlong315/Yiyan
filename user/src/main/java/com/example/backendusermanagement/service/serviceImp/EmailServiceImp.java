package com.example.backendusermanagement.service.serviceImp;

import com.example.backendusermanagement.mapper.UserMapper;
import com.example.backendusermanagement.model.domain.Code;
import com.example.backendusermanagement.model.domain.User;
import com.example.backendusermanagement.service.EmailService;
import freemarker.template.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Resource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

import static com.example.backendusermanagement.service.serviceImp.ManageServiceImp.SECRET_KEY;

@Service
public class EmailServiceImp extends EmailService {
    @Resource
    UserMapper userMapper;

    public int activate(String token) {
        System.out.println("token : " + token);
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            System.out.println("hah");
            if (claims != null) {
                String id = claims.getSubject();
                User user = userMapper.getUserById(id);
                Date expiration = claims.getExpiration();
                Date activate_at = new Date();
                long timeDifferenceMillis = activate_at.getTime() - expiration.getTime();
                long secondsDifference = timeDifferenceMillis / 1000;
                System.out.println(4);
                if (secondsDifference > 300) {
                    return -1;//token已过期
                }
                userMapper.updateUser(user);
                userMapper.deleteInactiveUser(user.getEmail());
            } else {
                return -2;//token解析失败
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
        return 0;
    }

    @Autowired
    private TemplateEngine templateEngine;

    public boolean sendEmail1(String to, String subject, String templateName, Map<String, String> model){
        try{
            JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
            javaMailSenderImpl.setDefaultEncoding("utf-8");
            javaMailSenderImpl.setHost("smtp.qq.com");                // host
            javaMailSenderImpl.setPort(465);                        // 端口
            javaMailSenderImpl.setUsername("aamofe@qq.com");        // 账户
            javaMailSenderImpl.setPassword("kckahqszucbpgifh");        // 密码
            javaMailSenderImpl.setProtocol("smtps");                // 协议
            Properties properties = new Properties();
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.starttls.required", "true");
            javaMailSenderImpl.setJavaMailProperties(properties);
            MimeMessage message = javaMailSenderImpl.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(new InternetAddress(MimeUtility.encodeText("「易研」学术") + "<aamofe@qq.com>"));
            helper.setTo(to);
            helper.setSubject(subject);

            Context context = new Context();
            Set<String> keys = model.keySet();
            for (String key : keys) {
                context.setVariable(key, model.get(key));
//            System.out.println(key+" "+model.get(key));
            }
            String text = templateEngine.process(templateName, context);
            helper.setText(text, true);
            // 发送邮件
            try {
                javaMailSenderImpl.send(message);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}