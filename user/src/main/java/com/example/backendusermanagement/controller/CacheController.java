//package com.example.backendusermanagement.controller;
//
//import com.example.backendusermanagement.service.CacheService;
//import jakarta.annotation.Resource;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//public class CacheControler {
//    @RestController
//    public class RocketMQController {
//
//        @Resource
//        CacheService cacheService;
//
//
//        @RequestMapping("/cache")
//        public String cache(@RequestParam String key, @RequestParam String value){
//            cacheService.add(key,value);
//            return "ok";
//        }
//
//        @RequestMapping("/cacheGet")
//        public String cacheGet(@RequestParam String key){
//            return cacheService.get(key);
//        }
//
//
//    }
//
//}
