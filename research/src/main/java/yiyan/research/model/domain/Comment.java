package yiyan.research.model.domain;

import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Data
public class Comment {
    private int id;
    private int userId;
    private Date createdAt;
    private String content;
    private int parentId;
    private String workId;
    public Map<String, Object> toDict() {
        Map<String, Object> commentMap = new TreeMap<>();
        commentMap.put("id", this.id);
        commentMap.put("userId", this.userId);
        commentMap.put("createdAt", this.createdAt);
        commentMap.put("content", this.content);
        commentMap.put("parentId", this.parentId);
        commentMap.put("workId", this.workId);
        return commentMap;
    }
}
/*
CREATE TABLE Comment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    createdAt DATETIME NOT NULL,
    content TEXT NOT NULL,
    parentId INT,
    workId varchar(50) NOT NULL
);

 */