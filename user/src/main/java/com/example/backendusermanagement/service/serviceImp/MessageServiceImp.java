package com.example.backendusermanagement.service.serviceImp;

import com.example.backendusermanagement.mapper.MessageMapper;
import com.example.backendusermanagement.model.domain.Message;
import com.example.backendusermanagement.service.ManageService;
import com.example.backendusermanagement.service.MessageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
public class MessageServiceImp extends MessageService {
    @Resource
    MessageMapper messageMapper;
    @Resource
    ManageService manageService;
    @Override
    public List<Map<String, Object>> getAllMessage(int userId) {
        Message [] messages = messageMapper.getAllMessage(userId);
        List<Map<String, Object>>map=new ArrayList<>();
        for(Message m:messages){
            Map<String, Object> ma=m.toDict();
            int id= (int) ma.get("srcId");
            String name=manageService.getUserById(String.valueOf(id)).getUsername();
            System.out.println("妈妈 "+id+" "+name);
            ma.put("srcName",name);
//            ma.put("dstName",manageService.getUserById((String) ma.get("srcId")));
            map.add(ma);
        }
        return map;
    }


    @Override
    public boolean readMessage(int userId, int messageId) {
        Message m=messageMapper.getMessageById(messageId);
        if(m==null ||m.getDstId()!=userId)
            return false;
        messageMapper.readMessage(messageId);
        return true;
    }

    @Override
    public boolean deleteMessage(int userId, int messageId) {
        Message m=messageMapper.getMessageById(messageId);
        if(m==null ||m.getDstId()!=userId)
            return false;
        messageMapper.deleteMessage(messageId);
        return true;
    }

    @Override
    public void readAllMessage(int userId) {
        messageMapper.readAllMessage(userId);
    }

    @Override
    public boolean createMessage(int type, int srcId, int dstId, String content) {
//        System.out.println(" 在创建message");
        Message m=new Message();
        Date createdAt=new Date();
        m.setType(type);
        m.setDstId(dstId);
        m.setSrcId(srcId);
        m.setContent(content);
        m.setCreatedAt(createdAt);
        messageMapper.insertMessage(m);
        return true;
    }
}
