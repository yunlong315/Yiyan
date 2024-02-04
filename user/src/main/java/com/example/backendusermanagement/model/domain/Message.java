package com.example.backendusermanagement.model.domain;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Data
public class Message {
    private int id;             // 消息id
    private int dstId;         // 接收者id
    private int type;           // 消息类型（申请结果，投诉结果，被投诉通知，被评论通知，被点赞通知）
    private String content;     // 消息内容
    private Date createdAt;  // 创建时间
    private Boolean isRead=false;
    private int srcId;         // 来源（系统或点赞者id）
    public Map<String, Object> toDict() {
        Map<String, Object> messageMap = new TreeMap<>();
        messageMap.put("id", id);
        messageMap.put("dstId", dstId);
        messageMap.put("type", type);
        messageMap.put("content", content);
        messageMap.put("createdAt", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdAt));
        messageMap.put("srcId", srcId);
        messageMap.put("isRead",isRead);
        return messageMap;
    }
}
/*
1:投诉结果
2；被评论通知
3：被回复通知
4：被点赞通知
 */

/*
CREATE TABLE message (
    id INT PRIMARY KEY,
    userId INT NOT NULL,
    type INT NOT NULL,
    content TEXT,
    createdAt DATETIME,
    source INT,
    -- 添加其他可能的约束和索引
);

 */
