package com.example.backendusermanagement.service;

import java.util.List;
import java.util.Map;

public abstract class MessageService {
    public abstract List<Map<String, Object>> getAllMessage(int userId);

    public abstract boolean readMessage(int userId,int messageId);

    public abstract boolean deleteMessage(int userId, int messageId);
    public abstract void  readAllMessage(int userId);
    public abstract boolean createMessage(int type,int srcId,int dstId,String content );
}
