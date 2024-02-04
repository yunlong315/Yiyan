package com.example.administration.model.reply;

import com.example.administration.model.domain.ComplaintForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jdk.dynalink.linker.LinkerServices;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data

public class ComplaintReply {
    private int id;
    private final int userId;
    private final int type;//投诉评论(0),投诉回复(1)，投诉文献(2)，投诉附件(3), 投诉门户冒领(4),投诉文献冒领(5)
    private final String content;
    private final String targetId;
    private int status = 0;//待处理(0),通过(1),拒绝(2)
    private final int adminId;
    private String reply;
    private List<String> attachmentUrls;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;
    private String wrongPaperId;//冒领的文献

    public ComplaintReply(ComplaintForm complaintForm) {
        this.id = complaintForm.getId();
        this.userId = complaintForm.getUserId();
        this.type = complaintForm.getType();
        this.content = complaintForm.getContent();
        this.targetId = complaintForm.getTargetId();
        this.status = complaintForm.getStatus();
        this.adminId = complaintForm.getAdminId();
        this.reply = complaintForm.getReply();
        this.attachmentUrls = Arrays.asList(complaintForm.getAttachmentUrls().split(",\\s*"));;
        System.out.println(attachmentUrls);
        this.createTime = complaintForm.getCreateTime();
        this.updateTime = complaintForm.getUpdateTime();
        this.wrongPaperId = complaintForm.getWrongPaperId();
    }

}
