//package com.example.backendusermanagement.controller;
//
//import com.example.backendusermanagement.model.Message;
//import com.example.backendusermanagement.model.msgContent.LoginContent;
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//public class RocketMQController {
//    @Autowired
//    RocketMQTemplate rocketMQTemplate;
//    //发送普通消息
//    @RequestMapping("/isLogin")
//    public String send(@RequestParam String token){
//        Message msg=new Message<>();
//        LoginContent content=new LoginContent(token);
//        msg.setContent(content);
//        /*
//        researcher-topic
//         */
//        rocketMQTemplate.convertAndSend("user-topic",msg);
//        return "ok";
//    }
//
//
//}
//
//
//
