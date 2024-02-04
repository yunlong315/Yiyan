package com.example.administration.model.reply;

import com.example.administration.model.domain.ComplaintForm;
import com.example.administration.model.domain.RequestForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
public class RequestReply {
    private int id;
    private final int userId;
    private final int type;//1:申请成为作者，2:反馈建议
    private final String content;
    private final String targetId;
    private int status =1;//1:正在审批 2:申请通过 3:申请拒绝
    private final int adminId;
    private String reply;
    private final List<String> attachmentUrls;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private final LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    public RequestReply(RequestForm requestForm) {
        this.id = requestForm.getId();
        this.userId = requestForm.getUserId();
        this.type = requestForm.getType();
        this.content = requestForm.getContent();
        this.targetId = requestForm.getTargetId();
        this.status = requestForm.getStatus();
        this.adminId = requestForm.getAdminId();
        this.reply = requestForm.getReply();
        this.attachmentUrls = Arrays.asList(requestForm.getAttachmentUrls().split(",\\s*"));;
        System.out.println(attachmentUrls);
        this.createTime = requestForm.getCreateTime();
        this.updateTime = requestForm.getUpdateTime();
    }
}
