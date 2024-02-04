package com.example.administration.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class Article {
    private int id;
    private String name;
    private final Integer adminId;
    private String label;
    private String concepts;
    private String text;
    private List<String> relateWorkIds;
    private String imageUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    //create时用
    public Article(String name, int adminId, String label, String concepts, String text, String imageUrl,List<String> relateWorkIds, LocalDateTime createTime) {
        this.name = name;
        this.adminId = adminId;
        this.label = label;
        this.concepts = concepts;
        this.text = text;
        this.imageUrl = imageUrl;
        this.relateWorkIds = relateWorkIds;
        this.createTime = createTime;
        this.updateTime = createTime;
    }
    //update时用
    public Article(int id,String name,String label,String concepts,String text,String imageUrl,List<String> relateWorkIds,LocalDateTime updateTime){
        //不允许更新的字段
        this.adminId = null;
        this.createTime = null;
        this.id = id;
        this.name = name;
        this.label = label;
        this.concepts =concepts;
        this.text = text;
        this.imageUrl = imageUrl;
        this.relateWorkIds = relateWorkIds;
        this.updateTime = updateTime;
    }
}
